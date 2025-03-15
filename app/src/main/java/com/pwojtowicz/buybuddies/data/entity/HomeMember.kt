package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
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
            parentColumns = ["firebaseUid"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class HomeMember(
    val homeId: Long,
    val userId: String,
    val role: MemberRole = MemberRole.MEMBER,
    override val updatedAt: Long = System.currentTimeMillis(),
    override val createdAt: String = "",
    override val syncedAt: Long = System.currentTimeMillis()
) : BaseEntity