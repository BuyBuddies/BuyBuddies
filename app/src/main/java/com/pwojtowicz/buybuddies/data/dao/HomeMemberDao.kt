package com.pwojtowicz.buybuddies.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pwojtowicz.buybuddies.data.entity.HomeMember
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeMemberDao {
    @Query("SELECT * FROM home_members WHERE homeId = :homeId")
    fun getMembersByHomeId(homeId: Long): Flow<List<HomeMember>>

    @Query("SELECT * FROM home_members WHERE userId = :userId")
    fun getHomesByUserId(userId: String): Flow<List<HomeMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: HomeMember)

    @Delete
    suspend fun delete(member: HomeMember)
}