package com.pwojtowicz.buybuddies.di

import android.content.Context
import androidx.room.Room
import com.pwojtowicz.buybuddies.data.dao.DepotDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListItemDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListLabelDao
import com.pwojtowicz.buybuddies.data.dao.HomeDao
import com.pwojtowicz.buybuddies.data.dao.HomeMemberDao
import com.pwojtowicz.buybuddies.data.dao.ItemCategoryDao
import com.pwojtowicz.buybuddies.data.dao.StoredItemDao
import com.pwojtowicz.buybuddies.data.dao.UserDao
import com.pwojtowicz.buybuddies.data.db.BuyBuddiesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBuyBUddiesDatabase(
        @ApplicationContext context: Context
    ): BuyBuddiesDatabase {
        return Room.databaseBuilder(
            context,
            BuyBuddiesDatabase::class.java,
            "buybuddies_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun provideUserDao(db: BuyBuddiesDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideHomeDao(db: BuyBuddiesDatabase): HomeDao = db.homeDao()

    @Provides
    @Singleton
    fun provideHomeMemberDao(db: BuyBuddiesDatabase): HomeMemberDao = db.homeMemberDao()

    @Provides
    @Singleton
    fun provideDepotDao(db: BuyBuddiesDatabase): DepotDao = db.depotDao()

    @Provides
    @Singleton
    fun provideGroceryListDao(db: BuyBuddiesDatabase): GroceryListDao = db.groceryListDao()

    @Provides
    @Singleton
    fun provideGroceryListLabelDao(db: BuyBuddiesDatabase): GroceryListLabelDao = db.groceryListLabelDao()

    @Provides
    @Singleton
    fun provideStoredItemDao(db: BuyBuddiesDatabase): StoredItemDao = db.storedItemDao()

    @Provides
    @Singleton
    fun provideGroceryListItemDao(db: BuyBuddiesDatabase): GroceryListItemDao = db.groceryListItemDao()

    @Provides
    @Singleton
    fun provideItemCategoryDao(db: BuyBuddiesDatabase): ItemCategoryDao = db.itemCategoryDao()
}