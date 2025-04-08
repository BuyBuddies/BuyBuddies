package com.pwojtowicz.buybuddies.auth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuestModeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val GUEST_MODE_KEY = "is_guest_mode"

    fun isGuestMode(): Boolean {
        return prefs.getBoolean(GUEST_MODE_KEY, false)
    }

    fun setGuestMode(isGuest: Boolean){
        prefs.edit().putBoolean(GUEST_MODE_KEY, isGuest).apply()
    }

    fun clearGuestMode() {
        prefs.edit().remove(GUEST_MODE_KEY).apply()
    }

}