package com.pwojtowicz.buybuddies.auth

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?,
    val isNewUser: Boolean = false
)