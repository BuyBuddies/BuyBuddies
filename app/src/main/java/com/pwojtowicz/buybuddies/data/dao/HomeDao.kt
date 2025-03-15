package com.pwojtowicz.buybuddies.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pwojtowicz.buybuddies.data.entity.Home
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    @Query("SELECT * FROM homes")
    fun getAll(): Flow<List<Home>>

    @Query("SELECT * FROM homes WHERE id = :id")
    fun getById(id: Long): Flow<Home>

    @Query("SELECT * FROM homes WHERE ownerId = :ownerId")
    fun getByOwnerId(ownerId: String): Flow<List<Home>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(home: Home): Long

    @Delete
    suspend fun delete(home: Home)

    @Query("DELETE FROM homes")
    suspend fun deleteAll()
}