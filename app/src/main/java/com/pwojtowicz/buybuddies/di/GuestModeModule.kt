package com.pwojtowicz.buybuddies.di

import android.content.Context
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.auth.GuestModeManager
import com.pwojtowicz.buybuddies.data.api.GroceryListApiService
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.network.sync.GuestDataMigrationService
import com.pwojtowicz.buybuddies.data.repository.GroceryListRepository
import com.pwojtowicz.buybuddies.data.repository.LocalIdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GuestModeModule {
    @Provides
    @Singleton
    fun provideLocalIdManager(
        @ApplicationContext context: Context
    ): LocalIdManager {
        return LocalIdManager(context)
    }

    @Provides
    @Singleton
    fun provideGuestDataMigrationService(
        guestModeManager: GuestModeManager,
        localIdManager: LocalIdManager,
        groceryListRepository: GroceryListRepository,
    ): GuestDataMigrationService {
        return GuestDataMigrationService(
            guestModeManager,
            localIdManager,
            groceryListRepository
        )
    }
}