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
    @Query("SELECT * FROM stored_items WHERE depotId = :depotId AND deletedAt IS NULL")
    fun getByDepotId(depotId: String): Flow<List<StoredItem>>

    @Query("SELECT * FROM stored_items WHERE id = :id")
    suspend fun getById(id: String): StoredItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(storedItem: StoredItem)

    @Delete
    suspend fun delete(storedItem: StoredItem)
}
