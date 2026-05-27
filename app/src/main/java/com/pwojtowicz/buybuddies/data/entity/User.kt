package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.enums.SyncStatus
import java.util.UUID

@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = UserAvatar::class,
            parentColumns = ["id"],
            childColumns = ["avatarId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["avatarId"]),
        Index(value = ["firebaseUid"], unique = true)
    ]
)
data class User(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    val firebaseUid: String? = null,
    val name: String = "",
    val email: String = "",
    val avatarId: String? = null,
    override val createdAt: Long = System.currentTimeMillis(),
    override val updatedAt: Long = System.currentTimeMillis(),
    override val syncedAt: Long = 0L,
    override val syncStatus: SyncStatus = SyncStatus.LOCAL_ONLY,
    override val deletedAt: Long? = null,
    override val version: Long = 0
) : BaseEntity
