package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.enums.SyncStatus
import java.util.UUID

@Entity(tableName = "depots")
data class Depot(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val homeId: String? = null,
    val ownerId: String,
    val barcodeNumber: String = "",
    override val createdAt: Long = System.currentTimeMillis(),
    override val updatedAt: Long = System.currentTimeMillis(),
    override val syncedAt: Long = 0L,
    override val syncStatus: SyncStatus = SyncStatus.LOCAL_ONLY,
    override val deletedAt: Long? = null,
    override val version: Long = 0
) : BaseEntity