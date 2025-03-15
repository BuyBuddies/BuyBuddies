package com.pwojtowicz.buybuddies.data.repository

import android.util.Log
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.data.api.GroceryListApiService
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.dto.GroceryListDTO
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject


class GroceryListRepository @Inject constructor(
    private val authClient: AuthorizationClient,
    private val groceryListApiService: GroceryListApiService,
    private val groceryListDao: GroceryListDao
) {
    suspend fun fetchUserLists() {
        Log.i(TAG, "Fetching user's grocery lists")
        try {
            val remoteLists = groceryListApiService.getMyLists()
            Log.d(TAG, "Received ${remoteLists.size} lists from remote")


            val entities = remoteLists.map { dto ->
                GroceryList(
                    id = dto.id,
                    name = dto.name.let { name ->
                        if (name.startsWith("\"") && name.endsWith("\"")) {
                            name.substring(1, name.length - 1)
                        } else {
                            name
                        }
                    },
                    description = dto.description,
                    ownerId = dto.ownerId,
                    listStatus = dto.status,
                    updatedAt = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis().toString(),
                )
            }

            groceryListDao.syncLists(entities)
            Log.i(TAG, "Successfully saved ${entities.size} lists to local DB")
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching grocery lists", e)
            throw handleApiError(e)
        }
    }

    suspend fun createGroceryList(groceryList: GroceryList): Long {
        Log.i(TAG, "Creating new grocery list: ${groceryList.name}")
        try {
            validateGroceryList(groceryList)?.let { throw it }

            val dto = GroceryListDTO(
                name = groceryList.name,
                description = groceryList.description,
                ownerId = groceryList.ownerId,
                homeId = groceryList.homeId,
                status = groceryList.listStatus,
                createdAt = groceryList.createdAt,
                updatedAt = groceryList.updatedAt
            )

            // Create on remote
            val createdList = groceryListApiService.createGroceryList(dto)
            Log.d(TAG, "Successfully created list on remote with ID: ${createdList.id}")

            // Save to local DB
            return groceryListDao.insert(groceryList.copy(id = createdList.id))
        } catch (e: Exception) {
            Log.e(TAG, "Error creating grocery list", e)
            throw handleApiError(e)
        }
    }

    suspend fun addMember(listId: Long, listName: String, email: String) {
        Log.i(TAG, "Adding member with email $email to list $listName")
        try {
            val currentList = groceryListDao.getById(listId)
                ?: throw IllegalStateException("List not found")

            val firebaseUid = authClient.getSignedInUser()?.firebaseUid
            val listDto = GroceryListDTO(
                id = currentList.id,
                name = currentList.name,
                description = currentList.description,
                ownerId = firebaseUid,
                homeId = currentList.homeId,
                status = currentList.listStatus,
                updatedAt = currentList.updatedAt,
                createdAt = currentList.createdAt
            )

            val updatedList = groceryListApiService.addMemberByEmail(listDto, email)
            Log.d(TAG, "Successfully added member on remote")
        } catch (e: Exception) {
            Log.e(TAG, "Error adding member to list", e)
            throw handleApiError(e)
        }
    }

    suspend fun updateListName(listId: Long, newName: String) {
        Log.i(TAG, "Updating list name for list $listId to: $newName")
        try {
            if (newName.isBlank()) {
                throw IllegalArgumentException("List name cannot be empty")
            }

            // Update on remote
            val updatedList = groceryListApiService.updateListName(listId, newName)
            Log.d(TAG, "Successfully updated list name on remote")

            // Update in local DB
            groceryListDao.getById(listId)?.let { localList ->
                groceryListDao.update(localList.copy(name = newName))
                Log.i(TAG, "Successfully updated list name in local DB")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating list name", e)
            throw handleApiError(e)
        }
    }

    suspend fun getListMembers(listId: Long): List<String> {
        Log.i(TAG, "Fetching members for list $listId")
        try {
            // Fetch from remote to ensure we have the latest data
            val members = groceryListApiService.getListMembers(listId)
            Log.d(TAG, "Received ${members.size} members from remote")
            return members
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching list members", e)
            throw handleApiError(e)
        }
    }



    suspend fun removeMember(listId: Long, email: String) {
        Log.i(TAG, "Removing member with email $email from list $listId")
        try {
            // The API service should handle converting email to Firebase UID
            val updatedList = groceryListApiService.removeMemberByEmail(listId, email)
            Log.d(TAG, "Successfully removed member on remote")

            // Update local list if needed
            groceryListDao.getById(listId)?.let { localList ->
                groceryListDao.update(localList)
                Log.i(TAG, "Successfully updated local list after member removal")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error removing member from list", e)
            throw handleApiError(e)
        }
    }

    suspend fun deleteGroceryList(listId: Long) {
        Log.i(TAG, "Deleting grocery list: $listId")
        try {
            groceryListApiService.deleteGroceryList(listId)
            Log.d(TAG, "Successfully deleted list from remote")
            groceryListDao.deleteById(listId)
            Log.i(TAG, "Successfully deleted list from local DB")

            fetchUserLists()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting grocery list", e)
            throw handleApiError(e)
        }
    }

    private suspend fun validateGroceryList(groceryList: GroceryList): Throwable? {
        return when {
            groceryList.name.isBlank() -> {
                Log.w(TAG, "Attempted to create list with empty name")
                IllegalArgumentException("List name cannot be empty")
            }

            groceryListDao.exists(groceryList.name, groceryList.ownerId) -> {
                Log.w(TAG, "List with name '${groceryList.name}' already exists for user ${groceryList.ownerId}")
                IllegalArgumentException("A list with this name already exists")
            }
            else -> null
        }
    }


    private fun handleApiError(e: Exception): Throwable {
        return when (e) {
            is HttpException -> when (e.code()) {
                401 -> IllegalStateException("Authentication failed - please log in again")
                403 -> IllegalStateException("Not authorized for this operation")
                404 -> IllegalStateException("List not found")
                else -> IllegalStateException("Server error: ${e.message}")
            }
            else -> e
        }
    }

    fun getListsForCurrentUser(userId: String): Flow<List<GroceryList>> {
        Log.d(TAG, "Getting grocery lists from local DB for user: $userId")
        return groceryListDao.getAll()
    }

    suspend fun getListNameById(groceryListId: Long): String {
        return groceryListDao.getListNameById(groceryListId)
            ?: throw RuntimeException("Grocery list with ID $groceryListId not found")
    }

    companion object {
        private const val TAG = "GroceryListRepository"
    }
}