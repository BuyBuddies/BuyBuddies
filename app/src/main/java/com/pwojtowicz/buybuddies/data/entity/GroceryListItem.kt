package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus
import java.time.LocalDateTime

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
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listId: Long = 0,
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: MeasurementUnit = MeasurementUnit.PIECE,
    val categoryId: Long? = null,
    val purchaseStatus: PurchaseStatus = PurchaseStatus.PENDING,
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = LocalDateTime.now().toString(),
    override val syncedAt: Long = System.currentTimeMillis()
) : BaseEntity