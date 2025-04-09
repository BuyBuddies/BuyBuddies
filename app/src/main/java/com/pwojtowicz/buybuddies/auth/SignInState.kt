package com.pwojtowicz.buybuddies.auth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val signInError: String? = null,
    val isGuestMode: Boolean = false,
    var user: UserData? = null,
    val migrationStatus: MigrationStatus = MigrationStatus.NONE,
    val migrationError: String? = null,
)

/**
 * Enum defining the status of guest data migration
 */
enum class MigrationStatus {
    NONE,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}