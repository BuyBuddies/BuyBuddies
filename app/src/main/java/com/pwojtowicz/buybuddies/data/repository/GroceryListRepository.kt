package com.pwojtowicz.buybuddies.data.repository

import android.util.Log
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.auth.GuestModeManager
import com.pwojtowicz.buybuddies.data.api.GroceryListApiService
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.dto.GroceryListDTO
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListStatus
import com.pwojtowicz.buybuddies.utility.toEpochMillis
import com.pwojtowicz.buybuddies.utility.toIsoString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

class GroceryListRepository @Inject constructor(
    private val authClient: AuthorizationClient,
    private val groceryListApiService: GroceryListApiService,
    private val groceryListDao: GroceryListDao,
    private val guestModeManager: GuestModeManager
) {
    companion object {
        private const val TAG = "GroceryListRepository"
    }

    suspend fun fetchUserLists() {
        Log.i(TAG, "Fetching user's grocery lists")

        if (guestModeManager.isGuestMode()) {
            Log.d(TAG, "Guest mode: using local lists only")
            return
        }

        try {
            val ownerId = authClient.getSignedInUser()?.firebaseUid ?: return
            val remoteLists = groceryListApiService.getMyLists()
            Log.d(TAG, "Received ${remoteLists.size} lists from remote")

            val entities = remoteLists.map { dto ->
                GroceryList(
                    id = dto.id,
                    name = dto.name.trimQuotes(),
                    description = dto.description,
                    ownerId = dto.ownerId,
                    listStatus = GroceryListStatus.entries.firstOrNull { it.name == dto.status }
                        ?: GroceryListStatus.ACTIVE,
                    updatedAt = dto.updatedAt.toEpochMillis(),
                    createdAt = dto.createdAt.toEpochMillis(),
                    syncStatus = com.pwojtowicz.buybuddies.data.enums.SyncStatus.SYNCED,
                    syncedAt = System.currentTimeMillis()
                )
            }

            groceryListDao.syncLists(ownerId, entities)
            Log.i(TAG, "Synced ${entities.size} lists to local DB")
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching grocery lists", e)
            throw handleApiError(e)
        }
    }

    suspend fun createGroceryList(groceryList: GroceryList): String {
        Log.i(TAG, "Creating new grocery list: ${groceryList.name}")

        try {
            validateGroceryList(groceryList)?.let { throw it }

            if (guestModeManager.isGuestMode()) {
                Log.d(TAG, "Guest mode: Creating list locally only")
                val guestUserId = guestModeManager.getGuestUserId()
                val localList = groceryList.copy(ownerId = guestUserId)
                groceryListDao.insert(localList)
                return localList.id
            }

            val dto = GroceryListDTO(
                name = groceryList.name,
                description = groceryList.description,
                ownerId = groceryList.ownerId,
                homeId = groceryList.homeId,
                status = groceryList.listStatus.name,
                createdAt = groceryList.createdAt.toIsoString(),
                updatedAt = groceryList.updatedAt.toIsoString()
            )

            val createdList = groceryListApiService.createGroceryList(dto)
            Log.d(TAG, "Created list on remote with ID: ${createdList.id}")

            val synced = groceryList.copy(
                id = createdList.id,
                syncStatus = com.pwojtowicz.buybuddies.data.enums.SyncStatus.SYNCED,
                syncedAt = System.currentTimeMillis()
            )
            groceryListDao.insert(synced)
            return synced.id
        } catch (e: Exception) {
            Log.e(TAG, "Error creating grocery list", e)
            throw handleApiError(e)
        }
    }

    suspend fun addMember(listId: String, listName: String, email: String) {
        Log.i(TAG, "Adding member $email to list $listName")
        if (guestModeManager.isGuestMode()) throw IllegalStateException("Member management not available in guest mode")

        try {
            val currentList = groceryListDao.getById(listId)
                ?: throw IllegalStateException("List not found")
            val listDto = currentList.toDto(authClient.getSignedInUser()?.firebaseUid)
            groceryListApiService.addMemberByEmail(listDto, email)
            Log.d(TAG, "Successfully added member on remote")
        } catch (e: Exception) {
            Log.e(TAG, "Error adding member to list", e)
            throw handleApiError(e)
        }
    }

    suspend fun updateListName(listId: String, newName: String) {
        Log.i(TAG, "Updating list name for $listId to: $newName")
        if (newName.isBlank()) throw IllegalArgumentException("List name cannot be empty")

        try {
            if (guestModeManager.isGuestMode()) {
                groceryListDao.getById(listId)?.let {
                    groceryListDao.update(it.copy(name = newName, updatedAt = System.currentTimeMillis()))
                } ?: throw IllegalStateException("List not found")
                return
            }

            groceryListApiService.updateListName(listId, newName)
            groceryListDao.getById(listId)?.let {
                groceryListDao.update(it.copy(name = newName, updatedAt = System.currentTimeMillis()))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating list name", e)
            throw handleApiError(e)
        }
    }

    suspend fun getListMembers(listId: String): List<String> {
        Log.i(TAG, "Fetching members for list $listId")
        if (guestModeManager.isGuestMode()) {
            return listOf(guestModeManager.getCurrentGuestUserId() ?: "")
        }
        return try {
            groceryListApiService.getListMembers(listId)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching list members", e)
            throw handleApiError(e)
        }
    }

    suspend fun removeMember(listId: String, email: String) {
        Log.i(TAG, "Removing member $email from list $listId")
        if (guestModeManager.isGuestMode()) throw IllegalStateException("Member management not available in guest mode")

        try {
            groceryListApiService.removeMemberByEmail(listId, email)
            Log.d(TAG, "Successfully removed member on remote")
        } catch (e: Exception) {
            Log.e(TAG, "Error removing member from list", e)
            throw handleApiError(e)
        }
    }

    suspend fun deleteGroceryList(listId: String) {
        Log.i(TAG, "Deleting grocery list: $listId")
        try {
            if (guestModeManager.isGuestMode()) {
                groceryListDao.deleteById(listId)
                return
            }
            groceryListApiService.deleteGroceryList(listId)
            groceryListDao.deleteById(listId)
            fetchUserLists()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting grocery list", e)
            throw handleApiError(e)
        }
    }

    suspend fun getListNameById(groceryListId: String): String {
        return groceryListDao.getListNameById(groceryListId)
            ?: throw RuntimeException("Grocery list $groceryListId not found")
    }

    suspend fun changeStatus(groceryListId: String, status: GroceryListStatus) {
        groceryListDao.getById(groceryListId)?.let {
            groceryListDao.update(it.copy(listStatus = status, updatedAt = System.currentTimeMillis()))
        }
    }

    fun getListsForCurrentUser(userId: String): Flow<List<GroceryList>> {
        val effectiveUserId = if (guestModeManager.isGuestMode()) {
            guestModeManager.getCurrentGuestUserId() ?: userId
        } else {
            userId
        }
        Log.d(TAG, "Getting grocery lists for user: $effectiveUserId")
        return groceryListDao.getListsForUser(effectiveUserId)
    }

    suspend fun migrateGuestDataToAuthenticatedUser(authenticatedUserId: String) {
        val guestUserId = guestModeManager.getCurrentGuestUserId() ?: return
        Log.i(TAG, "Migrating lists from guest $guestUserId to $authenticatedUserId")

        val guestLists = groceryListDao.getByOwnerId(guestUserId).first()
        if (guestLists.isEmpty()) return

        guestLists.forEach { guestList ->
            try {
                val dto = GroceryListDTO(
                    name = guestList.name,
                    description = guestList.description,
                    ownerId = authenticatedUserId,
                    homeId = guestList.homeId,
                    status = guestList.listStatus.name,
                    createdAt = guestList.createdAt.toIsoString(),
                    updatedAt = guestList.updatedAt.toIsoString()
                )
                val createdList = groceryListApiService.createGroceryList(dto)
                groceryListDao.delete(guestList)
                groceryListDao.insert(
                    guestList.copy(
                        id = createdList.id,
                        ownerId = authenticatedUserId,
                        syncStatus = com.pwojtowicz.buybuddies.data.enums.SyncStatus.SYNCED
                    )
                )
                Log.d(TAG, "Migrated list '${guestList.name}' → ${createdList.id}")
            } catch (e: Exception) {
                Log.e(TAG, "Error migrating list '${guestList.name}'", e)
            }
        }
        Log.i(TAG, "Migration complete")
    }

    private suspend fun validateGroceryList(groceryList: GroceryList): Throwable? = when {
        groceryList.name.isBlank() -> IllegalArgumentException("List name cannot be empty")
        groceryListDao.exists(groceryList.name, groceryList.ownerId) ->
            IllegalArgumentException("A list with this name already exists")
        else -> null
    }

    private fun GroceryList.toDto(overrideOwnerId: String?): GroceryListDTO = GroceryListDTO(
        id = id,
        name = name,
        description = description,
        ownerId = overrideOwnerId ?: ownerId,
        homeId = homeId,
        status = listStatus.name,
        createdAt = createdAt.toIsoString(),
        updatedAt = updatedAt.toIsoString()
    )

    private fun String.trimQuotes() =
        if (startsWith("\"") && endsWith("\"")) substring(1, length - 1) else this

    private fun handleApiError(e: Exception): Throwable {
        if (guestModeManager.isGuestMode()) {
            return if (e is HttpException) {
                IllegalStateException("This operation requires an internet connection in guest mode")
            } else e
        }
        return when (e) {
            is HttpException -> when (e.code()) {
                401 -> IllegalStateException("Authentication failed - please sign in again")
                403 -> IllegalStateException("Not authorized for this operation")
                404 -> IllegalStateException("List not found")
                else -> IllegalStateException("Server error: ${e.message}")
            }
            else -> e
        }
    }
}
