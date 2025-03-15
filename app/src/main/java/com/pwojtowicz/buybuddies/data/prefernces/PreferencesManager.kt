package com.pwojtowicz.buybuddies.data.prefernces

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    var isFirstInstall: Boolean
        get() = prefs.getBoolean(KEY_FIRST_INSTALL, true)
        set(value) {
            prefs.edit().putBoolean(KEY_FIRST_INSTALL, value).apply()
        }

    companion object {
        private const val KEY_FIRST_INSTALL = "is_first_install"
    }
}