package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.data.enums.SyncStatus
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color
import java.util.UUID

@Entity(tableName = "grocery_list_labels")
data class GroceryListLabel(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    val name: String,
    val color: String = bb_theme_main_color.toString(),
    override val createdAt: Long = System.currentTimeMillis(),
    override val updatedAt: Long = System.currentTimeMillis(),
    override val syncedAt: Long = 0L,
    override val syncStatus: SyncStatus = SyncStatus.LOCAL_ONLY,
    override val deletedAt: Long? = null,
    override val version: Long = 0
) : BaseEntity
