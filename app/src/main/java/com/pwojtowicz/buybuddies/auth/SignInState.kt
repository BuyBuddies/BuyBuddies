package com.pwojtowicz.buybuddies.auth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val signInError: String? = null
)