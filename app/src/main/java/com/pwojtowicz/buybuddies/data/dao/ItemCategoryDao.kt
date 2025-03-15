package com.pwojtowicz.buybuddies.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pwojtowicz.buybuddies.data.entity.ItemCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemCategoryDao {
    @Query("SELECT * FROM item_categories")
    fun getAll(): Flow<List<ItemCategory>>

    @Query("SELECT * FROM item_categories WHERE id = :id")
    fun getById(id: Long): Flow<ItemCategory?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: ItemCategory): Long

    @Delete
    suspend fun delete(category: ItemCategory)
}