package com.pwojtowicz.buybuddies.data.dto

data class HomeDTO(
    val id: String? = null,
    val name: String,
    val description: String?,
    val ownerId: String,
    val membersIds: List<String> = emptyList(),
    override val updatedAt: String?,
    override val createdAt: String?
) : BaseDto
