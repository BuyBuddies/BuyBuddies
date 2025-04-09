package com.pwojtowicz.buybuddies.data.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import java.util.UUID

@Singleton
class LocalIdManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("local_id_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val LOCAL_ID_PREFIX = "local_"
        private const val GUEST_USER_ID_KEY = "guest_user_id"
        private const val NEXT_LOCAL_ID_KEY = "next_local_id"
        private const val TAG = "LocalIdManager"
    }

    /**
     * Check if the provided ID is a locally-generated ID
     *  Local Id has stored val < 0
     **/
    fun isLocalId(id: Long): Boolean {
        return id < 0
    }

    fun generateLocalId(): Long {
        val nextId = prefs.getLong(NEXT_LOCAL_ID_KEY, -1)
        val newId = nextId - 1

        prefs.edit() { putLong(NEXT_LOCAL_ID_KEY, newId) }

        Log.d(TAG, "Generated new local ID: $newId")
        return newId
    }

    /**
     * Get or create guest user ID
     * This is is used o associate entities with the guest user
     */
    fun getOrCreateGuestUserId(): String {
        val existingId = prefs.getString(GUEST_USER_ID_KEY, null)

        if(!existingId.isNullOrEmpty()) {
            return existingId
        }

        val newGuestId = "guest_${UUID.randomUUID()}"
        prefs.edit {
            putString(GUEST_USER_ID_KEY, newGuestId)
        }

        Log.d(TAG, "Created new guest user ID: $newGuestId")
        return newGuestId
    }

    fun clear() {
        prefs.edit {
            clear()
        }

        Log.d(TAG, "Cleared all local IDs and guest user ID")
    }
}