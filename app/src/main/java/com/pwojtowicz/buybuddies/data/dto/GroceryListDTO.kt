package com.pwojtowicz.buybuddies.data.dto

data class GroceryListDTO(
    val id: String = "",
    val name: String,
    val description: String = "",
    val ownerId: String?,
    val homeId: String?,
    val status: String,
    val memberIds: Set<String> = emptySet(),
    override val updatedAt: String?,
    override val createdAt: String?
) : BaseDto
