package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus
import com.pwojtowicz.buybuddies.data.enums.SyncStatus
import java.util.UUID

@Entity(
    tableName = "grocery_items",
    foreignKeys = [
        ForeignKey(
            entity = GroceryList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("listId"),
        Index(value = ["listId", "name"], unique = true)
    ]
)
data class GroceryListItem(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    val listId: String = "",
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: MeasurementUnit = MeasurementUnit.PIECE,
    val categoryId: String? = null,
    val purchaseStatus: PurchaseStatus = PurchaseStatus.PENDING,
    override val createdAt: Long = System.currentTimeMillis(),
    override val updatedAt: Long = System.currentTimeMillis(),
    override val syncedAt: Long = 0L,
    override val syncStatus: SyncStatus = SyncStatus.LOCAL_ONLY,
    override val deletedAt: Long? = null,
    override val version: Long = 0
) : BaseEntity
