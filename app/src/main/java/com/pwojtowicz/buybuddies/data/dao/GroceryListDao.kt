package com.pwojtowicz.buybuddies.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryListDao {
    @Query("SELECT * FROM grocery_lists WHERE deletedAt IS NULL")
    fun getAll(): Flow<List<GroceryList>>

    @Query("""
        SELECT * FROM grocery_lists
        WHERE deletedAt IS NULL
          AND (ownerId = :userId OR id IN (
              SELECT groceryListId FROM grocery_list_members WHERE memberId = :userId
          ))
    """)
    fun getListsForUser(userId: String): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE homeId = :homeId AND deletedAt IS NULL")
    fun getByHomeId(homeId: String): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE ownerId = :ownerId AND deletedAt IS NULL")
    fun getByOwnerId(ownerId: String): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE deletedAt IS NULL ORDER BY sortOrder ASC")
    fun getAllGroceryListsSorted(): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE id = :id")
    suspend fun getById(id: String): GroceryList?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryList: GroceryList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(groceryLists: List<GroceryList>)

    @Query("SELECT EXISTS(SELECT 1 FROM grocery_lists WHERE name = :name AND ownerId = :ownerId AND deletedAt IS NULL)")
    suspend fun exists(name: String, ownerId: String?): Boolean

    @Query("SELECT COUNT(*) FROM homes WHERE id = :homeId")
    suspend fun homeExists(homeId: String): Int

    @Query("SELECT COUNT(*) FROM users WHERE id = :userId")
    suspend fun userExists(userId: String): Int

    @Update
    suspend fun update(groceryList: GroceryList)

    @Update
    suspend fun updateGroceryLists(groceryLists: List<GroceryList>)

    @Delete
    suspend fun delete(groceryList: GroceryList)

    @Query("DELETE FROM grocery_lists WHERE ownerId = :ownerId")
    suspend fun deleteAllByOwnerId(ownerId: String)

    @Query("DELETE FROM grocery_lists")
    suspend fun deleteAll()

    @Query("SELECT * FROM grocery_lists WHERE ownerId = :ownerId ORDER BY updatedAt DESC")
    suspend fun getLatestByOwnerId(ownerId: String): List<GroceryList>

    @Query("DELETE FROM grocery_lists WHERE id = :groceryListId")
    suspend fun deleteById(groceryListId: String)

    @Transaction
    suspend fun syncLists(ownerId: String, lists: List<GroceryList>) {
        deleteAllByOwnerId(ownerId)
        insertAll(lists)
    }

    @Query("SELECT name FROM grocery_lists WHERE id = :groceryListId")
    suspend fun getListNameById(groceryListId: String): String?

    @Query("UPDATE grocery_lists SET name = :newName, updatedAt = :updatedAt WHERE id = :listId")
    suspend fun updateListName(listId: String, newName: String, updatedAt: Long = System.currentTimeMillis())
}