package com.pwojtowicz.buybuddies.data.dao

import androidx.room.*
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryListItemDao {
    @Query("SELECT * FROM grocery_items WHERE deletedAt IS NULL")
    fun getAll(): Flow<List<GroceryListItem>>

    @Query("SELECT * FROM grocery_items WHERE listId = :listId AND deletedAt IS NULL")
    fun getByListId(listId: String): Flow<List<GroceryListItem>>

    @Query("SELECT * FROM grocery_items WHERE id = :id")
    fun getById(id: String): Flow<GroceryListItem>

    @Query("SELECT EXISTS(SELECT 1 FROM grocery_items WHERE name = :name AND listId = :listId AND deletedAt IS NULL)")
    suspend fun exists(name: String, listId: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM grocery_items WHERE listId = :listId AND name = :name AND deletedAt IS NULL)")
    suspend fun existsByListIdAndName(listId: String, name: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryListItem: GroceryListItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GroceryListItem>)

    @Update
    suspend fun update(groceryListItem: GroceryListItem)

    @Delete
    suspend fun delete(groceryListItem: GroceryListItem)

    @Query("DELETE FROM grocery_items WHERE listId = :listId")
    suspend fun deleteByListId(listId: String)

    @Query("DELETE FROM grocery_items")
    suspend fun deleteAll()

    @Transaction
    suspend fun syncItems(listId: String, items: List<GroceryListItem>) {
        deleteByListId(listId)
        insertAll(items)
    }
}
