package com.pwojtowicz.buybuddies.data.network.sync

import android.util.Log
import com.pwojtowicz.buybuddies.auth.GuestModeManager
import com.pwojtowicz.buybuddies.data.repository.GroceryListRepository
import com.pwojtowicz.buybuddies.data.repository.LocalIdManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuestDataMigrationService @Inject constructor(
    private val guestModeManager: GuestModeManager,
    private val localIdManager: LocalIdManager,
    private val groceryListRepository: GroceryListRepository
) {
    companion object {
        private const val TAG = "GuestDataMigration"
    }

    /**
     * Migrates all data created while in guest mode to the authenticated user account
     * @param authenticatedUserId The Firebase UID of the authenticated user
     */
    suspend fun migrateGuestData(authenticatedUserId: String) {
        Log.i(TAG, "Starting guest data migration to authenticated user: $authenticatedUserId")

        if (!guestModeManager.isGuestMode()) {
            Log.d(TAG, "Not in guest mode, no migration needed")
            return
        }

        try {
            groceryListRepository.migrateGuestDataToAuthenticatedUser(authenticatedUserId)
            // TODO: rest of migrations

            guestModeManager.clearGuestMode()
            localIdManager.clear()

            Log.i(TAG, "Guest data migration completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error during guest data migration", e)
            throw e
        }
    }
}