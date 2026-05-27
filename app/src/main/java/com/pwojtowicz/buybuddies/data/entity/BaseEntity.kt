package com.pwojtowicz.buybuddies.data.entity

import com.pwojtowicz.buybuddies.data.enums.SyncStatus

interface BaseEntity {
    val id: String
    val createdAt: Long
    val updatedAt: Long
    val syncedAt: Long
    val syncStatus: SyncStatus
    val deletedAt: Long?
    val version: Long

    fun needsSync(): Boolean = syncStatus == SyncStatus.PENDING || syncStatus == SyncStatus.FAILED
    fun isLocalOnly(): Boolean = syncStatus == SyncStatus.LOCAL_ONLY
    fun isSynced(): Boolean = syncStatus == SyncStatus.SYNCED
    fun isDeleted(): Boolean = deletedAt != null
}
