package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import com.pwojtowicz.buybuddies.data.enums.SyncStatus
import java.util.UUID

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
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    val depotId: String,
    val name: String,
    val description: String? = null,
    val quantity: Double,
    val unit: MeasurementUnit = MeasurementUnit.PIECE,
    val categoryId: String? = null,
    val expirationDate: Long? = null,
    override val createdAt: Long = System.currentTimeMillis(),
    override val updatedAt: Long = System.currentTimeMillis(),
    override val syncedAt: Long = 0L,
    override val syncStatus: SyncStatus = SyncStatus.LOCAL_ONLY,
    override val deletedAt: Long? = null,
    override val version: Long = 0
) : BaseEntity
