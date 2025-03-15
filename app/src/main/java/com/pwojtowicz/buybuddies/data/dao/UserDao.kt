package com.pwojtowicz.buybuddies.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pwojtowicz.buybuddies.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE firebaseUid = :firebaseUid")
    fun getByFirebaseUid(firebaseUid: String): User

    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id: Long): Flow<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Query("DELETE FROM users WHERE firebaseUid = :firebaseUid")
    suspend fun deleteByFirebaseUid(firebaseUid: String)

    @Transaction
    suspend fun syncUser(user: User) {
        deleteByFirebaseUid(user.firebaseUid)
        insert(user)
    }
}
