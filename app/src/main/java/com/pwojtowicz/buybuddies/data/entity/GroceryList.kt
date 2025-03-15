package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "grocery_lists",
    foreignKeys = [
        ForeignKey(
            entity = Home::class,
            parentColumns = ["id"],
            childColumns = ["homeId"],
            onDelete = ForeignKey.SET_NULL,
            deferred = true
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["firebaseUid"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )
    ],
    indices = [
        Index("homeId"),
        Index("ownerId")
    ]
)
data class GroceryList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ownerId: String? = null,
    val homeId: Long? = null,
    val name: String = "",
    val description: String = "",
    val listStatus: String = GroceryListStatus.ACTIVE.name,
    var sortOrder: Int = 0,
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = "",
    override val syncedAt: Long = System.currentTimeMillis()
) : BaseEntity

enum class GroceryListStatus {
    ACTIVE,
    DROPPED,
    DONE
}
