package com.pwojtowicz.buybuddies.data.dto

data class DepotDTO(
    val id: String = "",
    val name: String,
    val description: String?,
    val homeId: String,
    override val updatedAt: String?,
    override val createdAt: String?
) : BaseDto
