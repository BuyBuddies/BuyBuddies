package com.pwojtowicz.buybuddies.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pwojtowicz.buybuddies.data.entity.StoredItem
import kotlinx.coroutines.flow.Flow

@Dao
interface StoredItemDao {
    @Query("SELECT * FROM stored_items WHERE depotId = :depotId")
    fun getByDepotId(depotId: Long): Flow<List<StoredItem>>

    @Query("SELECT * FROM stored_items WHERE id = :id")
    fun getById(id: Long): Flow<StoredItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(storedItem: StoredItem): Long

    @Delete
    suspend fun delete(storedItem: StoredItem)
}