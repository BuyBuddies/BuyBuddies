package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.enums.SyncStatus
import java.util.UUID

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
            parentColumns = ["id"],
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
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    val ownerId: String? = null,
    val homeId: String? = null,
    val name: String = "",
    val description: String = "",
    val listStatus: GroceryListStatus = GroceryListStatus.ACTIVE,
    val sortOrder: Int = 0,
    override val createdAt: Long = System.currentTimeMillis(),
    override val updatedAt: Long = System.currentTimeMillis(),
    override val syncedAt: Long = 0L,
    override val syncStatus: SyncStatus = SyncStatus.LOCAL_ONLY,
    override val deletedAt: Long? = null,
    override val version: Long = 0
) : BaseEntity

enum class GroceryListStatus {
    ACTIVE,
    DROPPED,
    DONE
}
