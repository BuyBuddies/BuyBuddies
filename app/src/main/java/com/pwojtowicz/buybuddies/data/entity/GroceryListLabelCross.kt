package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "grocery_list_label_cross_ref",
    primaryKeys = ["groceryListId", "labelId"],
    foreignKeys = [
        ForeignKey(
            entity = GroceryList::class,
            parentColumns = ["id"],
            childColumns = ["groceryListId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GroceryListLabel::class,
            parentColumns = ["id"],
            childColumns = ["labelId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GroceryListLabelCross(
    val groceryListId: Long,
    val labelId: Long,
)