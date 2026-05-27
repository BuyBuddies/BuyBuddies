package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.pwojtowicz.buybuddies.data.enums.MemberRole

@Entity(
    tableName = "grocery_list_members",
    primaryKeys = ["groceryListId", "memberId"],
    foreignKeys = [
        ForeignKey(
            entity = GroceryList::class,
            parentColumns = ["id"],
            childColumns = ["groceryListId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("memberId")]
)
data class GroceryListMember(
    val groceryListId: String,
    val memberId: String,
    val role: MemberRole = MemberRole.MEMBER,
    val joinedAt: Long = System.currentTimeMillis()
)
