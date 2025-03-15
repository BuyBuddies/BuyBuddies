package com.pwojtowicz.buybuddies.data.network.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pwojtowicz.buybuddies.data.repository.GroceryListRepository
import com.pwojtowicz.buybuddies.data.repository.GroceryListItemRepository
import com.pwojtowicz.buybuddies.data.repository.HomeRepository
import com.pwojtowicz.buybuddies.data.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val groceryListRepository: GroceryListRepository,
    private val groceryListItemRepository: GroceryListItemRepository,
    private val homeRepository: HomeRepository,
    private val userRepository: UserRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            groceryListRepository.fetchUserLists()
//            homeRepository.fetchData()
//            userRepository.fetchUserData()
//            groceryListItemRepository.fetchGroceryListItems()

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Periodic sync failed", e)
            Result.retry()
        }
    }

    companion object {
        private const val TAG = "DataSyncWorker"
        const val WORK_NAME = "periodic_sync"
    }
}