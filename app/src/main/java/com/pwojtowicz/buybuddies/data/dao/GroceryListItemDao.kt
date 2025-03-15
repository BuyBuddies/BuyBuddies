package com.pwojtowicz.buybuddies.data.dao

import androidx.room.*
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import kotlinx.coroutines.flow.Flow


@Dao
interface GroceryListItemDao {
    @Query("SELECT * FROM grocery_items")
    fun getAll(): Flow<List<GroceryListItem>>

    @Query("SELECT * FROM grocery_items WHERE listId = :listId")
    fun getByListId(listId: Long): Flow<List<GroceryListItem>>

    @Query("SELECT * FROM grocery_items WHERE id = :id")
    fun getById(id: Long): Flow<GroceryListItem>

    @Query("SELECT EXISTS(SELECT 1 FROM grocery_items WHERE name = :name AND listId = :listId)")
    suspend fun  exists(name: String, listId: Long): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM grocery_items WHERE listId = :listId AND name = :name)")
    suspend fun existsByListIdAndName(listId: Long, name: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryListItem: GroceryListItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GroceryListItem>)

    @Update
    suspend fun update(groceryListItem: GroceryListItem)

    @Delete
    suspend fun delete(groceryListItem: GroceryListItem)

    @Query("DELETE FROM grocery_items")
    suspend fun deleteAll()

    @Transaction
    suspend fun syncItems(items: List<GroceryListItem>) {
        deleteAll()
        insertAll(items)
    }
}
