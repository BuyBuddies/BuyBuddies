package com.pwojtowicz.buybuddies.data.network.sync;

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSyncManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    fun setupPeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<DataSyncWorker>(
            15, TimeUnit.MINUTES,
            5, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            DataSyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun requestImmediateSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<DataSyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            "immediate_sync",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelSync() {
        workManager.cancelUniqueWork(DataSyncWorker.WORK_NAME)
        workManager.cancelUniqueWork("immediate_sync")
    }
}