package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.enums.FriendRequestStatus


@Entity(
    tableName = "friend_requests",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["firebaseUid"],
            childColumns = ["fromUserId"]
        )
    ]
)
data class FriendRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fromUserId: String,
    val toUserId: String,
    val status: FriendRequestStatus,
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = "",
    override val syncedAt: Long = System.currentTimeMillis()
) : BaseEntity