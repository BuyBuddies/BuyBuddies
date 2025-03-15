package com.pwojtowicz.buybuddies

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.pwojtowicz.buybuddies.data.network.sync.DataSyncManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BuyBuddiesApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: WorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
//        WorkManager.initialize(this, workManagerConfiguration)
    }
}