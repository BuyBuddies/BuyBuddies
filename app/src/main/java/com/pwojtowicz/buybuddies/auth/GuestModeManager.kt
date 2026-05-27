package com.pwojtowicz.buybuddies.auth

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.pwojtowicz.buybuddies.data.dao.UserDao
import com.pwojtowicz.buybuddies.data.entity.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

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

    fun isGuestMode(): Boolean = prefs.getBoolean(GUEST_MODE_KEY, false)

    fun getCurrentGuestUserId(): String? {
        if (!isGuestMode()) return null
        return prefs.getString(GUEST_USER_ID_KEY, null)
    }

    suspend fun getGuestUserId(): String {
        if (!isGuestMode()) throw IllegalStateException("Not in guest mode")
        val storedId = prefs.getString(GUEST_USER_ID_KEY, null)
        if (storedId != null && userDao.getById(storedId) != null) {
            return storedId
        }
        return createGuestUser()
    }

    suspend fun setGuestMode(isGuest: Boolean) {
        if (isGuest) {
            try {
                val guestUserId = createGuestUser()
                Log.d(TAG, "Created guest user with ID: $guestUserId")
                prefs.edit {
                    putBoolean(GUEST_MODE_KEY, true)
                    putString(GUEST_USER_ID_KEY, guestUserId)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to create guest user, cannot enter guest mode", e)
                throw e
            }
        } else {
            clearGuestMode()
        }
    }

    suspend fun clearGuestMode() {
        val guestId = prefs.getString(GUEST_USER_ID_KEY, null)
        if (guestId != null) {
            userDao.getById(guestId)?.let { userDao.delete(it) }
        }
        prefs.edit {
            remove(GUEST_MODE_KEY)
            remove(GUEST_USER_ID_KEY)
        }
    }

    private suspend fun createGuestUser(): String {
        val existingId = prefs.getString(GUEST_USER_ID_KEY, null)
        if (existingId != null) {
            val existing = userDao.getById(existingId)
            if (existing != null) {
                Log.d(TAG, "Using existing guest user: $existingId")
                return existingId
            }
            Log.d(TAG, "Stale guest ID in prefs, creating new guest user")
        }

        val guestUser = User(
            firebaseUid = null,
            name = "Guest",
            email = ""
        )

        try {
            userDao.insert(guestUser)
            val created = userDao.getById(guestUser.id)
                ?: throw IllegalStateException("Failed to verify guest user creation")

            prefs.edit { putString(GUEST_USER_ID_KEY, created.id) }
            Log.d(TAG, "Created guest user with ID: ${created.id}")
            return created.id
        } catch (e: Exception) {
            Log.e(TAG, "Error creating guest user", e)
            throw IllegalStateException("Failed to create guest user: ${e.message}")
        }
    }
}
