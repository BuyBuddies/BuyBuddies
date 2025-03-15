package com.pwojtowicz.buybuddies.utility

import android.content.Context
import android.util.Log
import com.pwojtowicz.buybuddies.data.prefernces.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstallManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) {
    fun isNewInstall(): Boolean {
        val isFirstLaunch = preferencesManager.isFirstInstall

        Log.d("InstallManager", "Is new install: $isFirstLaunch (FirstLaunch: $isFirstLaunch)")

        if (isFirstLaunch) {
            preferencesManager.isFirstInstall = false
        }

        return isFirstLaunch
    }
}