package com.pwojtowicz.buybuddies.data.dto

data class StoredItemDTO(
    val id: String? = null,
    val groceryItemName: String,
    val depotId: String,
    val depotName: String,
    val quantity: Double,
    val unit: String,
    val expirationDate: String,
    override val updatedAt: String?,
    override val createdAt: String?
) : BaseDto
