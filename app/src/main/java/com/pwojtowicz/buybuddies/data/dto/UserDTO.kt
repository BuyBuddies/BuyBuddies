package com.pwojtowicz.buybuddies.data.dto

data class UserDTO(
    val id: String? = null,
    val firebaseUid: String,
    val email: String,
    val name: String,
    override val updatedAt: String?,
    override val createdAt: String?
) : BaseDto
