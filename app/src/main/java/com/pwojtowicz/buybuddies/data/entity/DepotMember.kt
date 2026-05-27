package com.pwojtowicz.buybuddies.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.pwojtowicz.buybuddies.data.enums.MemberRole

@Entity(
    tableName = "depot_members",
    primaryKeys = ["depotId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = Depot::class,
            parentColumns = ["id"],
            childColumns = ["depotId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("depotId")]
)
data class DepotMember(
    val depotId: String,
    val userId: String,
    val role: MemberRole = MemberRole.MEMBER,
    val joinedAt: Long = System.currentTimeMillis()
)