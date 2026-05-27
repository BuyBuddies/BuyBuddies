package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.pwojtowicz.buybuddies.data.enums.MemberRole

@Entity(
    tableName = "home_members",
    primaryKeys = ["homeId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = Home::class,
            parentColumns = ["id"],
            childColumns = ["homeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class HomeMember(
    val homeId: String,
    val userId: String,
    val role: MemberRole = MemberRole.MEMBER,
    val joinedAt: Long = System.currentTimeMillis()
)