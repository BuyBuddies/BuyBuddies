package com.pwojtowicz.buybuddies.auth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class GuestModeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun isGuestMode(): Boolean {
        return prefs.getBoolean(GUEST_MODE_KEY, false)
    }

    fun setGuestMode(isGuest: Boolean){
        prefs.edit {
            putBoolean(GUEST_MODE_KEY, isGuest)
        }
    }

    fun clearGuestMode() {
        prefs.edit {
            remove(GUEST_MODE_KEY).apply()
        }
    }

    companion object {
        private const val GUEST_MODE_KEY = "is_guest_mode"
    }
}