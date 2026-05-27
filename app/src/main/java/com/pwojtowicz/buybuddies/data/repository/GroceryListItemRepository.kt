package com.pwojtowicz.buybuddies.data.repository

import android.util.Log
import com.pwojtowicz.buybuddies.auth.GuestModeManager
import com.pwojtowicz.buybuddies.data.api.GroceryListItemApiService
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListItemDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListLabelDao
import com.pwojtowicz.buybuddies.data.dto.GroceryListItemDTO
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabel
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import com.pwojtowicz.buybuddies.data.enums.SyncStatus
import com.pwojtowicz.buybuddies.utility.toEpochMillis
import com.pwojtowicz.buybuddies.utility.toIsoString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

class GroceryListItemRepository @Inject constructor(
    private val groceryListDao: GroceryListDao,
    private val groceryListItemDao: GroceryListItemDao,
    private val groceryListLabelDao: GroceryListLabelDao,
    private val guestModeManager: GuestModeManager,
    private val groceryListItemApiService: GroceryListItemApiService
) {
    companion object {
        private const val TAG = "GroceryListItemRepository"
    }

    // ### Grocery List Operations ###

    fun getAllGroceryLists(): Flow<List<GroceryList>> = groceryListDao.getAllGroceryListsSorted()

    suspend fun insertGroceryList(groceryList: GroceryList) {
        try {
            groceryListDao.insert(groceryList)
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting GroceryList", e)
            throw e
        }
    }

    suspend fun deleteGroceryList(groceryList: GroceryList) {
        groceryListDao.delete(groceryList)
    }

    suspend fun fetchGroceryItemsByListId(listId: String) {
        Log.i(TAG, "Fetching items for list $listId")
        try {
            val remoteItems = groceryListItemApiService.getItemsByList(listId)
            Log.d(TAG, "Received ${remoteItems.size} items from remote")

            val entities = remoteItems.map { dto -> dto.toEntity() }
            groceryListItemDao.syncItems(listId, entities)
            Log.i(TAG, "Synced ${entities.size} items for list $listId")
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching grocery list items", e)
            throw handleApiError(e)
        }
    }

    suspend fun fetchGroceryListItems() {
        Log.i(TAG, "Fetching all user grocery items")

        if (guestModeManager.isGuestMode()) {
            Log.d(TAG, "Guest mode: using local items only")
            return
        }

        try {
            val remoteItems = groceryListItemApiService.getListItemByUser()
            Log.d(TAG, "Received ${remoteItems.size} items from remote")

            // Group by listId and sync each list separately (scoped delete)
            remoteItems.groupBy { it.groceryListId }.forEach { (listId, dtos) ->
                val entities = dtos.map { it.toEntity() }
                groceryListItemDao.syncItems(listId, entities)
            }
            Log.i(TAG, "Synced items across ${remoteItems.groupBy { it.groceryListId }.size} lists")
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching grocery list items", e)
            throw handleApiError(e)
        }
    }

    suspend fun createGroceryListItem(groceryListItem: GroceryListItem): String {
        Log.i(TAG, "Creating item: ${groceryListItem.name}")

        try {
            validateGroceryListItem(groceryListItem)?.let { throw it }

            if (guestModeManager.isGuestMode()) {
                Log.d(TAG, "Guest mode: Creating item locally only")
                groceryListItemDao.insert(groceryListItem)
                return groceryListItem.id
            }

            val dto = GroceryListItemDTO(
                groceryListId = groceryListItem.listId,
                groceryItemName = groceryListItem.name,
                quantity = groceryListItem.quantity,
                unit = groceryListItem.unit.name,
                status = groceryListItem.purchaseStatus,
                createdAt = groceryListItem.createdAt.toIsoString(),
                updatedAt = groceryListItem.updatedAt.toIsoString()
            )

            val createdItem = groceryListItemApiService.createListItem(dto)
            Log.d(TAG, "Created item on remote: ${createdItem.id}")

            val synced = groceryListItem.copy(
                id = createdItem.id,
                syncStatus = SyncStatus.SYNCED,
                syncedAt = System.currentTimeMillis()
            )
            groceryListItemDao.insert(synced)
            return synced.id
        } catch (e: Exception) {
            Log.e(TAG, "Error creating grocery list item", e)
            throw handleApiError(e)
        }
    }

    suspend fun updateLocalItem(groceryListItem: GroceryListItem) {
        try {
            groceryListItemDao.update(groceryListItem)
            Log.d(TAG, "Updated local item: ${groceryListItem.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating local item", e)
            throw e
        }
    }

    suspend fun updateRemoteItem(groceryListItem: GroceryListItem) {
        if (guestModeManager.isGuestMode()) return

        try {
            val dto = GroceryListItemDTO(
                id = groceryListItem.id,
                groceryListId = groceryListItem.listId,
                groceryItemName = groceryListItem.name,
                quantity = groceryListItem.quantity,
                unit = groceryListItem.unit.name,
                status = groceryListItem.purchaseStatus,
                createdAt = groceryListItem.createdAt.toIsoString(),
                updatedAt = System.currentTimeMillis().toIsoString()
            )
            val updated = groceryListItemApiService.createOrUpdateGroceryListItem(dto)
            Log.d(TAG, "Updated remote item: ${updated.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating remote item", e)
            throw handleApiError(e)
        }
    }

    suspend fun updateItem(groceryListItem: GroceryListItem) {
        try {
            updateLocalItem(groceryListItem)
            updateRemoteItem(groceryListItem)
        } catch (e: Exception) {
            Log.e(TAG, "Error during full item update", e)
            throw e
        }
    }

    // ### Grocery Item Operations ###

    fun getAllGroceryItemsByListId(listId: String): Flow<List<GroceryListItem>> =
        groceryListItemDao.getByListId(listId)

    suspend fun updateGroceryItem(groceryListItem: GroceryListItem) =
        groceryListItemDao.update(groceryListItem)

    suspend fun insertGroceryItem(groceryListItem: GroceryListItem) =
        groceryListItemDao.insert(groceryListItem)

    suspend fun deleteGroceryItem(groceryListItem: GroceryListItem) {
        Log.i(TAG, "Deleting item ${groceryListItem.id} from list ${groceryListItem.listId}")
        try {
            if (!guestModeManager.isGuestMode()) {
                val itemDTO = GroceryListItemDTO(
                    id = groceryListItem.id,
                    groceryListId = groceryListItem.listId,
                    groceryItemName = groceryListItem.name,
                    quantity = groceryListItem.quantity,
                    unit = groceryListItem.unit.name,
                    status = groceryListItem.purchaseStatus,
                    createdAt = groceryListItem.createdAt.toIsoString(),
                    updatedAt = groceryListItem.updatedAt.toIsoString()
                )
                val response = groceryListItemApiService.deleteGroceryItem(itemDTO)
                if (!response.isSuccessful) {
                    throw Exception("Failed to delete item: ${response.code()}")
                }
                Log.d(TAG, "Deleted item from remote")
            }
            groceryListItemDao.delete(groceryListItem)
            Log.i(TAG, "Deleted item from local DB")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting grocery item", e)
            throw handleApiError(e)
        }
    }

    suspend fun deleteAllGroceryItems() = groceryListItemDao.deleteAll()

    // ### GroceryListLabel Operations ###

    fun getAllGroceryListLabels(): Flow<List<GroceryListLabel>> = groceryListLabelDao.getAll()

    suspend fun getLabelById(id: String): GroceryListLabel? = groceryListLabelDao.getById(id)

    fun getLabelsForList(listId: String): Flow<List<GroceryListLabel>> =
        groceryListLabelDao.getLabelsForList(listId)

    private suspend fun validateGroceryListItem(item: GroceryListItem): Throwable? = when {
        item.name.isBlank() -> IllegalArgumentException("Item name cannot be empty")
        groceryListItemDao.exists(item.name, item.listId) ->
            IllegalArgumentException("An item with this name already exists in the list")
        else -> null
    }

    private fun GroceryListItemDTO.toEntity(): GroceryListItem = GroceryListItem(
        id = id.ifBlank { java.util.UUID.randomUUID().toString() },
        listId = groceryListId,
        name = groceryItemName,
        quantity = quantity,
        unit = MeasurementUnit.entries.firstOrNull { it.name == unit } ?: MeasurementUnit.PIECE,
        categoryId = null,
        purchaseStatus = status,
        updatedAt = updatedAt.toEpochMillis(),
        createdAt = createdAt.toEpochMillis(),
        syncStatus = SyncStatus.SYNCED,
        syncedAt = System.currentTimeMillis()
    )

    private fun handleApiError(e: Exception): Throwable = when (e) {
        is HttpException -> when (e.code()) {
            401 -> IllegalStateException("Authentication failed - please log in again")
            403 -> IllegalStateException("Not authorized for this operation")
            404 -> IllegalStateException("Item not found")
            else -> IllegalStateException("Server error: ${e.message}")
        }
        else -> e
    }
}
