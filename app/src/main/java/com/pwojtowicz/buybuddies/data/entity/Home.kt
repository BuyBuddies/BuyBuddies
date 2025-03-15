package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "homes")
data class Home(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val ownerId: String,
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = "",
    override val syncedAt: Long = System.currentTimeMillis()
) : BaseEntity