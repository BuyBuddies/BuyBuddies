package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color

@Entity(tableName = "grocery_list_labels")
data class GroceryListLabel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: String = bb_theme_main_color.toString(),
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = "",
    override val syncedAt: Long = System.currentTimeMillis()
) : BaseEntity