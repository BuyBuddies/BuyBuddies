package com.pwojtowicz.buybuddies.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pwojtowicz.buybuddies.data.dao.DepotDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListItemDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListLabelDao
import com.pwojtowicz.buybuddies.data.dao.HomeDao
import com.pwojtowicz.buybuddies.data.dao.HomeMemberDao
import com.pwojtowicz.buybuddies.data.dao.ItemCategoryDao
import com.pwojtowicz.buybuddies.data.dao.StoredItemDao
import com.pwojtowicz.buybuddies.data.dao.UserDao
import com.pwojtowicz.buybuddies.data.entity.Depot
import com.pwojtowicz.buybuddies.data.entity.DepotMember
import com.pwojtowicz.buybuddies.data.entity.FriendRequest
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabel
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabelCross
import com.pwojtowicz.buybuddies.data.entity.GroceryListMember
import com.pwojtowicz.buybuddies.data.entity.Home
import com.pwojtowicz.buybuddies.data.entity.HomeMember
import com.pwojtowicz.buybuddies.data.entity.ItemCategory
import com.pwojtowicz.buybuddies.data.entity.StoredItem
import com.pwojtowicz.buybuddies.data.entity.User
import com.pwojtowicz.buybuddies.data.entity.UserAvatar

@Database(
    entities = [
        User::class,
        UserAvatar::class,
        FriendRequest::class,
        Home::class,
        HomeMember::class,
        Depot::class,
        DepotMember::class,
        GroceryList::class,
        GroceryListMember::class,
        GroceryListLabel::class,
        GroceryListLabelCross::class,
        StoredItem::class,
        GroceryListItem::class,
        ItemCategory::class
    ],
    version = 5,
    exportSchema = false
)
abstract class BuyBuddiesDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun homeDao(): HomeDao
    abstract fun homeMemberDao(): HomeMemberDao
    abstract fun depotDao(): DepotDao
    abstract fun groceryListDao(): GroceryListDao
    abstract fun groceryListLabelDao(): GroceryListLabelDao
    abstract fun storedItemDao(): StoredItemDao
    abstract fun groceryListItemDao(): GroceryListItemDao
    abstract fun itemCategoryDao(): ItemCategoryDao
}