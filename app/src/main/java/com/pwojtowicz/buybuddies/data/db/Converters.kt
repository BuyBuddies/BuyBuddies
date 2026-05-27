package com.pwojtowicz.buybuddies.data.db

import androidx.room.TypeConverter
import com.pwojtowicz.buybuddies.data.entity.GroceryListStatus
import com.pwojtowicz.buybuddies.data.enums.FriendRequestStatus
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import com.pwojtowicz.buybuddies.data.enums.MemberRole
import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus
import com.pwojtowicz.buybuddies.data.enums.SyncStatus

class Converters {
    @TypeConverter fun syncStatusToString(v: SyncStatus): String = v.name
    @TypeConverter fun stringToSyncStatus(v: String): SyncStatus = SyncStatus.valueOf(v)

    @TypeConverter fun measurementUnitToString(v: MeasurementUnit): String = v.name
    @TypeConverter fun stringToMeasurementUnit(v: String): MeasurementUnit = MeasurementUnit.valueOf(v)

    @TypeConverter fun purchaseStatusToString(v: PurchaseStatus): String = v.name
    @TypeConverter fun stringToPurchaseStatus(v: String): PurchaseStatus = PurchaseStatus.valueOf(v)

    @TypeConverter fun memberRoleToString(v: MemberRole): String = v.name
    @TypeConverter fun stringToMemberRole(v: String): MemberRole = MemberRole.valueOf(v)

    @TypeConverter fun friendRequestStatusToString(v: FriendRequestStatus): String = v.name
    @TypeConverter fun stringToFriendRequestStatus(v: String): FriendRequestStatus = FriendRequestStatus.valueOf(v)

    @TypeConverter fun groceryListStatusToString(v: GroceryListStatus): String = v.name
    @TypeConverter fun stringToGroceryListStatus(v: String): GroceryListStatus = GroceryListStatus.valueOf(v)
}
