package com.pwojtowicz.buybuddies.data.repository

import android.util.Log
import com.pwojtowicz.buybuddies.data.api.GroceryListItemApiService
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListItemDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListLabelDao
import com.pwojtowicz.buybuddies.data.dto.GroceryListItemDTO
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabel
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.time.LocalDateTime
import javax.inject.Inject


class GroceryListItemRepository @Inject constructor (
    private val groceryListDao: GroceryListDao,
    private val groceryListItemDao: GroceryListItemDao,
    private val groceryListLabelDao: GroceryListLabelDao,
    private val groceryListItemApiService: GroceryListItemApiService
) {
    // ### Grocery List Operations ###
    fun getAllGroceryLists(): Flow<List<GroceryList>> = groceryListDao.getAllGroceryListsSorted()

    //fun getGroceryListsByLabelId(labelId: Long?): Flow<List<GroceryList>> { return groceryListDao.getGroceryListsByLabelId(labelId) }
    suspend fun insertGroceryList(groceryList: GroceryList): Long {
        try {
            return groceryListDao.insert(groceryList)
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting GroceryList", e)
            throw e
        }
    }

    suspend fun deleteGroceryList(groceryList: GroceryList) {
        groceryListDao.delete(groceryList)
    }

    suspend fun fetchGroceryItemsByListId(listId: Long) {
        Log.i(TAG, "Fetching user's grocery lists")
        try {
            val remoteListItems = groceryListItemApiService.getItemsByList(listId)
            Log.d(TAG, "Received ${remoteListItems.size} items from remote for listId: $listId")

            val entities = remoteListItems.map { dto ->
                GroceryListItem(
                    id = dto.id,
                    listId = dto.groceryListId,
                    name = dto.groceryItemName,
                    quantity = dto.quantity,
                    unit = MeasurementUnit.valueOf(dto.unit),
                    categoryId = null,
                    purchaseStatus = dto.status,
                    updatedAt = dto.updatedAt ?: System.currentTimeMillis(),
                    createdAt = dto.createdAt ?: LocalDateTime.now().toString()
                )
            }

            groceryListItemDao.syncItems(entities)
            Log.i(TAG, "Successfully synced ${entities.size} items to local DB")
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching grocery list items", e)
            throw handleApiError(e)
        }
    }

    suspend fun fetchGroceryListItems() {
        Log.i(TAG, "Fetching user's grocery lists")
        try {
            val remoteListItems = groceryListItemApiService.getListItemByUser()
            Log.d(TAG, "Received ${remoteListItems.size} lists items from remote")

            val validItems = remoteListItems.filterNot { dto ->
                groceryListItemDao.existsByListIdAndName(dto.groceryListId, dto.groceryItemName)
            }

            if (validItems.isNotEmpty()) {
                val entities = validItems.map { dto ->
                    GroceryListItem(
                        id = dto.id,
                        listId = dto.groceryListId,
                        name = dto.groceryItemName,
                        quantity = dto.quantity,
                        unit = MeasurementUnit.valueOf(dto.unit),
                        categoryId = null,
                        purchaseStatus = dto.status,
                        updatedAt = dto.updatedAt ?: System.currentTimeMillis(),
                        createdAt = dto.createdAt ?: LocalDateTime.now().toString()
                    )
                }

                groceryListItemDao.syncItems(entities)
                Log.i(TAG, "Successfully synced ${entities.size} items to local DB")
            } else {
                Log.i(TAG, "No new items to sync")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching grocery list items", e)
            throw handleApiError(e)
        }
    }

    suspend fun createGroceryListItem(groceryListItem: GroceryListItem): Long {
        Log.i(TAG, "Creating new grocery list item: ${groceryListItem.name}")
        try {
            validateGroceryListItem(groceryListItem)?.let { throw it }

            val dto = GroceryListItemDTO(
                groceryListId = groceryListItem.listId,
                groceryItemName = groceryListItem.name,
                quantity = groceryListItem.quantity,
                unit = groceryListItem.unit.toString(),
                status = groceryListItem.purchaseStatus,
                createdAt = groceryListItem.createdAt,
                updatedAt = groceryListItem.updatedAt
            )

            // Create on remote
            val createdItem = groceryListItemApiService.createListItem(dto)
            Log.d(TAG, "Successfully created list item on remote with ID: ${createdItem.id}")

            // Save to local DB
            return groceryListItemDao.insert(groceryListItem.copy(id = createdItem.id))
        } catch (e: Exception) {
            Log.e(TAG, "Error creating grocery list item", e)
            throw handleApiError(e)
        }
    }

    private suspend fun validateGroceryListItem(groceryListItem: GroceryListItem): Throwable? {
        return when {
            groceryListItem.name.isBlank() -> {
                Log.w(TAG, "Attempted to create list with empty name")
                IllegalArgumentException("List name cannot be empty")
            }
            groceryListItemDao.exists(groceryListItem.name, groceryListItem.listId) -> {
                Log.w(TAG, "List with name '${groceryListItem.name}' already exists for list ${groceryListItem.listId}")
                IllegalArgumentException("A list with this name already exists")
            }
            else -> null
        }
    }

    suspend fun updateLocalItem(groceryListItem: GroceryListItem) {
        try {
            groceryListItemDao.update(groceryListItem)
            Log.d(TAG, "Successfully updated local item: ${groceryListItem.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating local item", e)
            throw e
        }
    }

    suspend fun updateRemoteItem(activeGroceryListId: Long, groceryListItem: GroceryListItem) {
        try {
            val dto = GroceryListItemDTO(
                id = groceryListItem.id,
                groceryListId = groceryListItem.listId,
                groceryItemName = groceryListItem.name,
                quantity = groceryListItem.quantity,
                unit = groceryListItem.unit.toString(),
                status = groceryListItem.purchaseStatus,
                createdAt = groceryListItem.createdAt,
                updatedAt = System.currentTimeMillis()
            )

            val updatedItem = groceryListItemApiService.createOrUpdateGroceryListItem(
                listItemDTO = dto
            )
            Log.d(TAG, "Successfully updated remote item: ${updatedItem.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating remote item", e)
            throw handleApiError(e)
        }
    }

    suspend fun updateItem(groceryListItem: GroceryListItem) {
        try {
            updateRemoteItem(groceryListItem.listId, groceryListItem)
            updateLocalItem(groceryListItem)
        } catch (e: Exception) {
            Log.e(TAG, "Error during full item update", e)
            throw e
        }
    }

    // ### Grocery Item Operations ###
    fun getAllGroceryItemsByListId(listId: Long): Flow<List<GroceryListItem>> {
        return groceryListItemDao.getByListId(listId)
    }
    suspend fun updateGroceryItem(groceryListItem: GroceryListItem) {
        groceryListItemDao.update(groceryListItem)
    }
    suspend fun insertGroceryItem(groceryListItem: GroceryListItem) {
        groceryListItemDao.insert(groceryListItem)
    }
    suspend fun deleteGroceryItem(groceryListItem: GroceryListItem) {
        Log.i(TAG, "Deleting grocery item ${groceryListItem.id} from list ${groceryListItem.listId}")
        try {
            // Convert to DTO and delete from remote
            val itemDTO = GroceryListItemDTO(
                id = groceryListItem.id,
                groceryListId = groceryListItem.listId,
                groceryItemName = groceryListItem.name,
                quantity = groceryListItem.quantity,
                unit = groceryListItem.unit.toString(),
                status = groceryListItem.purchaseStatus,
                createdAt = groceryListItem.createdAt,
                updatedAt = groceryListItem.updatedAt
            )

            val response = groceryListItemApiService.deleteGroceryItem(itemDTO)
            Log.d(TAG, "Successfully deleted item from remote")
            if (response.isSuccessful) {
                groceryListItemDao.delete(groceryListItem)
                Log.i(TAG, "Successfully deleted item from local DB")
            } else {
                throw Exception("Failed to delete item: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting grocery item", e)
            throw handleApiError(e)
        }
    }
    suspend fun deleteAllGroceryItems() {
        groceryListItemDao.deleteAll()
    }

    // ### GroceryListLabel Operations ###
    fun getAllGroceryListLabels(): Flow<List<GroceryListLabel>> {
        return groceryListLabelDao.getAll()
    }

    suspend fun getLabelById(id: Long): Flow<GroceryListLabel> {
        return groceryListLabelDao.getById(id)
    }
    fun getLabelsForList(listId: Long): Flow<List<GroceryListLabel>> {
        return groceryListLabelDao.getLabelsForList(listId)
    }

    private fun handleApiError(e: Exception): Throwable {
        return when (e) {
            is HttpException -> when (e.code()) {
                401 -> IllegalStateException("Authentication failed - please log in again")
                403 -> IllegalStateException("Not authorized for this operation")
                404 -> IllegalStateException("list item not found")
                else -> IllegalStateException("Server error: ${e.message}")
            }
            else -> e
        }
    }


    companion object {
        private const val TAG = "GroceryListItemRepository"
    }
}
