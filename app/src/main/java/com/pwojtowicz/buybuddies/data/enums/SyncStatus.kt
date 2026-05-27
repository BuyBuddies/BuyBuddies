package com.pwojtowicz.buybuddies.data.enums

enum class SyncStatus {
    LOCAL_ONLY,    // Created locally, never synced (guest mode or offline)
    PENDING,       // Has changes waiting to sync
    SYNCING,       // Currently being synced
    SYNCED,        // Successfully synced
    CONFLICT,      // Sync conflict detected
    FAILED         // Sync failed, will retry
}