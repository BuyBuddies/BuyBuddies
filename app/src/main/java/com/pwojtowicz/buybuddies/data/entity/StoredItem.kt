package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "stored_items",
    foreignKeys = [
        ForeignKey(
            entity = Depot::class,
            parentColumns = ["id"],
            childColumns = ["depotId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class StoredItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val depotId: Long,
    val name: String,
    val description: String?,
    val quantity: Double,
    val unit: String,
    val categoryId: Long?,
    val expirationDate: Long?,
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = "",
    override val syncedAt: Long = System.currentTimeMillis()
) : BaseEntity