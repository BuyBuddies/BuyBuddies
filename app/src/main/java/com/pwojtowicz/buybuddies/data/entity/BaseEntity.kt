package com.pwojtowicz.buybuddies.data.entity

interface BaseEntity {
    val updatedAt: Long
    val createdAt: String
    val syncedAt: Long
}