package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.enums.FriendRequestStatus
import com.pwojtowicz.buybuddies.data.enums.SyncStatus
import java.util.UUID

@Entity(
    tableName = "friend_requests",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["fromUserId"]
        )
    ]
)
data class FriendRequest(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    val fromUserId: String,
    val toUserId: String,
    val status: FriendRequestStatus,
    override val createdAt: Long = System.currentTimeMillis(),
    override val updatedAt: Long = System.currentTimeMillis(),
    override val syncedAt: Long = 0L,
    override val syncStatus: SyncStatus = SyncStatus.LOCAL_ONLY,
    override val deletedAt: Long? = null,
    override val version: Long = 0
) : BaseEntity
