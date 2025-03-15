package com.pwojtowicz.buybuddies.di

import android.content.Context
import com.pwojtowicz.buybuddies.data.prefernces.PreferencesManager
import com.pwojtowicz.buybuddies.utility.InstallManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager = PreferencesManager(context)

    @Provides
    @Singleton
    fun provideInstallManager(
        @ApplicationContext context: Context,
        prefenceManager: PreferencesManager
    ): InstallManager = InstallManager(context, prefenceManager)
}