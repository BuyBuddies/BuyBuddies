package com.pwojtowicz.buybuddies.di

import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.data.api.AuthApiService
import com.pwojtowicz.buybuddies.data.api.GroceryListApiService
import com.pwojtowicz.buybuddies.data.api.GroceryListItemApiService
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListItemDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListLabelDao
import com.pwojtowicz.buybuddies.data.dao.HomeDao
import com.pwojtowicz.buybuddies.data.dao.UserDao
import com.pwojtowicz.buybuddies.data.repository.GroceryListItemRepository
import com.pwojtowicz.buybuddies.data.repository.GroceryListRepository
import com.pwojtowicz.buybuddies.data.repository.HomeRepository
import com.pwojtowicz.buybuddies.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        authClient: AuthorizationClient,
        authApiService: AuthApiService
    ): UserRepository = UserRepository(
        userDao,
        authClient,
        authApiService
    )

    @Provides
    @Singleton
    fun provideHomeRepository(
        homeDao: HomeDao
    ): HomeRepository = HomeRepository(
        homeDao
    )

    @Provides
    @Singleton
    fun provideGroceryListRepository(
        authClient: AuthorizationClient,
        groceryListApiService: GroceryListApiService,
        groceryListDao: GroceryListDao
    ): GroceryListRepository = GroceryListRepository(
        authClient,
        groceryListApiService,
        groceryListDao
    )

    @Provides
    @Singleton
    fun provideGroceryListItemRepository(
        groceryListDao: GroceryListDao,
        groceryListItemDao: GroceryListItemDao,
        groceryListLabelDao: GroceryListLabelDao,
        groceryListItemApiService: GroceryListItemApiService
    ): GroceryListItemRepository = GroceryListItemRepository(
        groceryListDao,
        groceryListItemDao,
        groceryListLabelDao,
        groceryListItemApiService
    )
}