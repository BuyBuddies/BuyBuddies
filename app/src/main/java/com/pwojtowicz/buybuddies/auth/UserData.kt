package com.pwojtowicz.buybuddies.auth

data class UserData(
    val firebaseUid: String,
    val username: String?,
    val email: String?,
    val profilePictureUrl: String?
)

