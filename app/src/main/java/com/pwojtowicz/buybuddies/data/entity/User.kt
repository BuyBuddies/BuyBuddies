package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    ],
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firebaseUid: String = "",
    val name: String = "",
    val email: String,
    val avatarId: Long? = null,
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = "",
    override val syncedAt: Long = System.currentTimeMillis()
) : BaseEntity