package com.pwojtowicz.buybuddies.viewmodel

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus
import com.pwojtowicz.buybuddies.data.repository.GroceryListItemRepository
import com.pwojtowicz.buybuddies.data.repository.GroceryListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class GroceryViewModel @Inject constructor(
   private val authorizationClient: AuthorizationClient,
   private val groceryListItemRepository: GroceryListItemRepository,
   private val groceryListRepository: GroceryListRepository
): ViewModel() {
   private val _unsavedChanges = mutableStateListOf<GroceryListItem>()
   val hasUnsavedChanges = derivedStateOf { _unsavedChanges.isNotEmpty() }

   private val _activeGroceryListId = MutableStateFlow<Long?>(null)
   val activeGroceryListId: StateFlow<Long?> = _activeGroceryListId

   private val _groceryListName = MutableStateFlow("")
   val groceryListName: StateFlow<String> = _groceryListName.asStateFlow()

   private val _groceryListItems = MutableStateFlow<List<GroceryListItem>>(emptyList())
   val groceryListItems: StateFlow<List<GroceryListItem>> = _groceryListItems.asStateFlow()

   private val _members = MutableStateFlow<List<String>>(emptyList())
   val members: StateFlow<List<String>> = _members.asStateFlow()

   private val _loading = MutableLiveData<Boolean>()
   val loading: LiveData<Boolean> = _loading

   private val _error = MutableLiveData<String?>()
   val error: LiveData<String?> = _error

   fun setActiveGroceryListId(listId: Long?) {
      _activeGroceryListId.value = listId
      if (listId != null) {
         fetchGroceryListItems(listId)
      } else {
         _groceryListItems.value = emptyList()
      }
   }

   fun createGroceryItem(
      listId: Long,
      name: String,
      quantity: Double = 0.0,
      unit: MeasurementUnit = MeasurementUnit.PIECE,
      status: PurchaseStatus = PurchaseStatus.PENDING
   ) {
      viewModelScope.launch {
         try {
            _loading.value = true
            val currentUser = authorizationClient.getSignedInUser()
               ?: throw IllegalStateException("No user signed in")

            val newGroceryListItem = GroceryListItem(
               listId = listId,
               name = name.trim(),
               quantity = quantity,
               unit = unit,
               purchaseStatus = status,
               createdAt = LocalDateTime.now().toString(),
               updatedAt = System.currentTimeMillis()
            )
            groceryListItemRepository.createGroceryListItem(newGroceryListItem)
            fetchGroceryListItems(listId)
         } catch (e: Exception) {
            Log.e(TAG, "Error creating grocery item", e)
            _error.value = when (e) {
               is IllegalStateException -> "Authentication error: ${e.message}"
               is IllegalArgumentException -> "Invalid input: ${e.message}"
               else -> "Failed to create item"
            }
         } finally {
            _loading.value = false
         }
      }
   }

   private fun fetchGroceryListItems(listId: Long) {
      viewModelScope.launch {
         try {
            _loading.value = true
            groceryListItemRepository.fetchGroceryItemsByListId(listId)
            groceryListItemRepository.getAllGroceryItemsByListId(listId)
               .collect { items ->
                  _groceryListItems.value = items
               }
         } catch (e: Exception) {
            Log.e(TAG, "Error fetching grocery items", e)
            _groceryListItems.value = emptyList()
            _error.value = "Failed to load items"
         } finally {
            _loading.value = false
         }
      }
   }

   fun updateListName(listId: Long, newName: String) {
      viewModelScope.launch {
         try {
            _loading.value = true
            groceryListRepository.updateListName(listId, newName)
            _groceryListName.value = newName
         } catch (e: Exception) {
            Log.e(TAG, "Error updating list name", e)
            _error.value = "Failed to update list name"
         } finally {
            _loading.value = false
         }
      }
   }

   fun addToUnsavedChanges(item: GroceryListItem) {
      val index = _unsavedChanges.indexOfFirst { it.id == item.id }
      if (index != -1) {
         _unsavedChanges[index] = item
      } else {
         _unsavedChanges.add(item)
      }
   }

   fun saveChanges() {
      viewModelScope.launch {
         try {
            _unsavedChanges.forEach { item ->
               activeGroceryListId.value?.let { groceryListItemRepository.updateRemoteItem(it, item) }
               groceryListItemRepository.updateLocalItem(item)
            }
            _unsavedChanges.clear()
         } catch (e: Exception) {
            Log.e(TAG, "Error saving changes", e)
         }
      }
   }

   fun discardChanges() {
      _unsavedChanges.clear()
      loadGroceryItems()
   }

   fun updateLocalItem(groceryListItem: GroceryListItem) {
      addToUnsavedChanges(groceryListItem)
      viewModelScope.launch {
         groceryListItemRepository.updateGroceryItem(groceryListItem)
      }
   }

   private fun loadGroceryItems() {
      _activeGroceryListId.value?.let { listId ->
         viewModelScope.launch {
            groceryListItemRepository.getAllGroceryItemsByListId(listId)
         }
      }
   }

   fun deleteGroceryItem(groceryListItem: GroceryListItem) {
      viewModelScope.launch {
         try {
            _loading.value = true
            groceryListItemRepository.deleteGroceryItem(groceryListItem)

             _activeGroceryListId.value?.let { listId ->
                 fetchGroceryListItems(listId)
             }
         } catch (e: Exception) {
            Log.e(TAG, "Error deleting grocery item", e)
            _error.value = "Failed to delete item"
         } finally {
            _loading.value = false
         }
      }
   }

   fun toggleGroceryItemChecked(groceryItem: GroceryListItem) {
      val updatedStatus = if (groceryItem.purchaseStatus == PurchaseStatus.PENDING) {
         PurchaseStatus.PURCHASED
      } else {
         PurchaseStatus.PENDING
      }

      val updatedItem = groceryItem.copy(purchaseStatus = updatedStatus)
      Log.i(TAG, "Sending updated item: $updatedItem")

      viewModelScope.launch {
         try {
            groceryListItemRepository.updateItem(updatedItem)
         } catch (e: Exception) {
            Log.e(TAG, "Error updating grocery list item", e)
         }
      }
   }

   fun deleteList(listId: Long) {
      viewModelScope.launch {
         try {
            _loading.value = true
            groceryListRepository.deleteGroceryList(listId)
         } catch (e: Exception) {
            Log.e(TAG, "Error deleting list", e)
            _error.value = "Failed to delete list"
         } finally {
            _loading.value = false
         }
      }
   }

   fun addMember(email: String) {
      val listId = _activeGroceryListId.value
      val listName = _groceryListName.value

      if (listId != null && listName.isNotEmpty()) {
         viewModelScope.launch {
            try {
               _loading.value = true
               groceryListRepository.addMember(listId, listName, email)
               fetchMembers(listId)
            } catch (e: Exception) {
               Log.e(TAG, "Error adding member", e)
               _error.value = "Failed to add member"
            } finally {
               _loading.value = false
            }
         }
      }
   }

   fun deleteMember(listId: Long, email: String) {
      viewModelScope.launch {
         try {
            _loading.value = true
            groceryListRepository.removeMember(listId, email)
            fetchMembers(listId)
         } catch (e: Exception) {
            Log.e(TAG, "Error removing member", e)
            _error.value = "Failed to remove member"
         } finally {
            _loading.value = false
         }
      }
   }

   fun fetchMembers(listId: Long) {
      viewModelScope.launch {
         try {
            val membersList = groceryListRepository.getListMembers(listId)
            _members.value = membersList
         } catch (e: Exception) {
            Log.e(TAG, "Error fetching members", e)
            _error.value = "Failed to load members"
            _members.value = emptyList()
         }
      }
   }

   fun updateGroceryItemName(groceryListItem: GroceryListItem, newName: String) {
      updateLocalItem(groceryListItem.copy(name = newName))
   }

   fun updateGroceryItemQuantity(groceryListItem: GroceryListItem, newQuantity: String) {
      val quantity = newQuantity.toDoubleOrNull() ?: 0.0
      updateLocalItem(groceryListItem.copy(quantity = quantity))
   }

   fun updateGroceryItemUnit(groceryListItem: GroceryListItem, newUnit: String) {
      try {
         val unit = MeasurementUnit.valueOf(newUnit)
         updateLocalItem(groceryListItem.copy(unit = unit))
      } catch (e: IllegalArgumentException) {
         Log.w(TAG, "Invalid unit used: {}", e)
      }
   }

   fun fetchGroceryListName(groceryListId: Long) {
      viewModelScope.launch {
         try {
            val name = groceryListRepository.getListNameById(groceryListId)
            _groceryListName.value = name
         } catch (e: RuntimeException) {
            _groceryListName.value = "List Name"
         }
      }
   }

   fun clearError() {
      _error.value = null
   }

   companion object {
      private const val TAG = "GroceryViewModel"
   }
}