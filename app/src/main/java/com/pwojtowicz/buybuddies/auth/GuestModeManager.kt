package com.pwojtowicz.buybuddies.auth

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import com.pwojtowicz.buybuddies.data.dao.UserDao
import com.pwojtowicz.buybuddies.data.entity.User
import java.util.UUID

@Singleton
class GuestModeManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDao: UserDao
) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val GUEST_MODE_KEY = "is_guest_mode"
        private const val GUEST_USER_ID_KEY = "guest_user_id"
        private const val TAG = "GuestModeManager"
    }

    fun isGuestMode(): Boolean {
        return prefs.getBoolean(GUEST_MODE_KEY, false)
    }

    suspend fun setGuestMode(isGuest: Boolean) {
        if (isGuest) {
            // Create guest user first, before setting guest mode
            try {
                val guestUserId = createGuestUser()
                Log.d(TAG, "Created guest user with ID: $guestUserId")

                // Only enable guest mode after successful user creation
                prefs.edit {
                    putBoolean(GUEST_MODE_KEY, true)
                    putString(GUEST_USER_ID_KEY, guestUserId)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to create guest user, cannot enter guest mode", e)
                throw e
            }
        } else {
            prefs.edit {
                putBoolean(GUEST_MODE_KEY, false)
            }
        }
    }
    suspend fun getGuestUserId(): String {
//        if (!isGuestMode()) return null
//
//        val guestUserId = prefs.getString(GUEST_USER_ID_KEY, null)
//        if (guestUserId != null) {
//            if (userDao.userExists(guestUserId)) {
//                return guestUserId
//            }
//        }
        return createGuestUser()
    }

    fun getCurrentGuestUserId(): String? {
        if (!isGuestMode()) return null
        return prefs.getString(GUEST_USER_ID_KEY, null)
    }

    private suspend fun createGuestUserNow(): String {
        val newId = "guest_${UUID.randomUUID()}"
        Log.d(TAG, "Creating new guest user with ID: $newId")

        val guestUser = User(
            firebaseUid = newId,
            name = "Guest User",
            email = "guest@example.com",
            createdAt = System.currentTimeMillis().toString(),
            updatedAt = System.currentTimeMillis()
        )

        try {
            // Insert the user
            val insertedId = userDao.insert(guestUser)
            Log.d(TAG, "Inserted guest user with row ID: $insertedId")

            // Immediately verify it was actually inserted
            val userExists = userDao.userExists(newId)
            Log.d(TAG, "Verified user exists in database: $userExists")

            if (!userExists) {
                throw IllegalStateException("User insert operation succeeded but user not found in database")
            }

            return newId
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create guest user", e)
            throw e
        }
    }

    private suspend fun createGuestUser(): String {
        val existingId = prefs.getString(GUEST_USER_ID_KEY, null)

        if (existingId != null) {
            try {
                val user = userDao.getUserByFirebaseUid(existingId)
                if (user != null) {
                    Log.d(TAG, "Using existing guest user: $existingId")
                    return existingId
                }
                Log.d(TAG, "Guest user ID in prefs doesn't exist in database: $existingId")
            } catch (e: Exception) {
                Log.e(TAG, "Error checking for existing guest user", e)
            }
        }

        // Create a new guest user
        val newId = "guest_${UUID.randomUUID()}"
        Log.d(TAG, "Creating new guest user with ID: $newId")

        val guestUser = User(
            firebaseUid = newId,
            name = "Guest User",
            email = "guest@example.com",
            createdAt = System.currentTimeMillis().toString(),
            updatedAt = System.currentTimeMillis()
        )

        try {
            val insertedId = userDao.insert(guestUser)
            Log.d(TAG, "Created guest user in database with row ID: $insertedId")

            val createdUser = userDao.getUserByFirebaseUid(newId)
            if (createdUser == null) {
                Log.e(TAG, "Failed to retrieve created guest user!")
                throw IllegalStateException("Failed to create guest user")
            }

            // Save to preferences
            prefs.edit {
                putString(GUEST_USER_ID_KEY, newId)
            }

            return newId
        } catch (e: Exception) {
            Log.e(TAG, "Error creating guest user", e)
            throw IllegalStateException("Failed to create guest user: ${e.message}")
        }
    }


    fun clearGuestMode() {
        prefs.edit {
            remove(GUEST_MODE_KEY)
        }
    }

}