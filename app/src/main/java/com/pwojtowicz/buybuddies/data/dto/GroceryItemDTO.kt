package com.pwojtowicz.buybuddies.data.dto

data class GroceryItemDTO(
    val id: Long,
    val name: String,
    val description: String?,
    val barcode: String?,
    val category: String,
    val unit: String,
    override val updatedAt: Long,
    override val createdAt: String
) : BaseDto
