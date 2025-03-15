package com.pwojtowicz.buybuddies.data.dto

data class GroceryListDTO(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val ownerId: String?,
    val homeId: Long?,
    val status: String,
    val memberIds: Set<String> = emptySet(),
    override val updatedAt: Long,
    override val createdAt: String
) : BaseDto