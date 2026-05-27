package com.pwojtowicz.buybuddies.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pwojtowicz.buybuddies.data.entity.Depot
import kotlinx.coroutines.flow.Flow

@Dao
interface DepotDao {
    @Query("SELECT * FROM depots WHERE deletedAt IS NULL")
    fun getAll(): Flow<List<Depot>>

    @Query("SELECT * FROM depots WHERE id = :id")
    suspend fun getById(id: String): Depot?

    @Query("SELECT * FROM depots WHERE homeId = :homeId AND deletedAt IS NULL")
    fun getByHomeId(homeId: String): Flow<List<Depot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(depot: Depot)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(depots: List<Depot>)

    @Update
    suspend fun update(depot: Depot)

    @Delete
    suspend fun delete(depot: Depot)

    @Query("DELETE FROM depots")
    suspend fun deleteAll()
}
