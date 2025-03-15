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
    @Query("SELECT * FROM grocery_lists")
    fun getAll(): Flow<List<GroceryList>>

//    @Query("SELECT * FROM grocery_lists WHERE ownerId = :userId")
//    fun getListsForUser(userId: String): Flow<List<GroceryList>>

    @Query("""
        SELECT * FROM grocery_lists
        WHERE ownerId = :userId
           OR id IN (
               SELECT groceryListId 
               FROM grocery_list_members
               WHERE memberId = :userId
           )
    """)
    fun getListsForUser(userId: String): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE homeId = :homeId")
    fun getByHomeId(homeId: Long): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE ownerId = :firebaseUid")
    fun getByOwnerId(firebaseUid: String): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists ORDER BY sortOrder ASC")
    fun getAllGroceryListsSorted(): Flow<List<GroceryList>>

    @Query("SELECT * FROM grocery_lists WHERE id = :id")
    suspend fun getById(id: Long): GroceryList?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryList: GroceryList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(groceryLists: List<GroceryList>)

    @Query("SELECT EXISTS(SELECT 1 FROM grocery_lists WHERE name = :name AND ownerId = :ownerId)")
    suspend fun exists(name: String, ownerId: String?): Boolean

    @Query("SELECT COUNT(*) FROM homes WHERE id = :homeId")
    suspend fun homeExists(homeId: Long): Int

    @Query("SELECT COUNT(*) FROM users WHERE firebaseUid = :uid")
    suspend fun userExists(uid: String): Int

    @Update
    suspend fun update(groceryList: GroceryList)

    @Update
    suspend fun updateGroceryLists(groceryLists: List<GroceryList>)

    @Delete
    suspend fun delete(groceryList: GroceryList)

    @Query("DELETE FROM grocery_lists WHERE ownerId = :firebaseUid")
    suspend fun deleteAllByOwnerId(firebaseUid: String)

    @Query("DELETE FROM grocery_lists")
    suspend fun deleteAll()

    @Query("SELECT * FROM grocery_lists WHERE ownerId = :firebaseUid ORDER BY updatedAt DESC")
    suspend fun getLatestByOwnerId(firebaseUid: String): List<GroceryList>

    @Query("DELETE FROM grocery_lists WHERE id = :groceryListId")
    suspend fun deleteById(groceryListId: Long)

    @Transaction
    suspend fun syncLists(lists: List<GroceryList>) {
        deleteAll()
        insertAll(lists)
    }

    @Query("SELECT name FROM grocery_lists WHERE id = :groceryListId")
    suspend fun getListNameById(groceryListId: Long): String?

    @Query("UPDATE grocery_lists SET name = :newName WHERE id = :listId")
    suspend fun updateListName(listId: Long, newName: String)
}
