package com.pwojtowicz.buybuddies.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabel
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabelCross
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryListLabelDao {
    @Query("SELECT * FROM grocery_list_labels")
    fun getAll(): Flow<List<GroceryListLabel>>

    @Query("SELECT * FROM grocery_list_labels WHERE id = :id")
    fun getById(id: Long): Flow<GroceryListLabel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groceryListLabel: GroceryListLabel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(groceryListLabels: List<GroceryListLabel>)

    @Update
    suspend fun update(groceryListLabel: GroceryListLabel)

    @Delete
    suspend fun delete(groceryListLabel: GroceryListLabel)

    @Query("DELETE FROM grocery_list_labels")
    suspend fun deleteAll()

    @Query("""
        SELECT gll.* FROM grocery_list_labels gll
        INNER JOIN grocery_list_label_cross_ref gllcr ON gll.id = gllcr.labelId
        WHERE gllcr.groceryListId = :listId
    """)
    fun getLabelsForList(listId: Long): Flow<List<GroceryListLabel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroceryListLabelCrossRef(crossRef: GroceryListLabelCross)

    @Delete
    suspend fun deleteGroceryListLabelCrossRef(crossRef: GroceryListLabelCross)
}