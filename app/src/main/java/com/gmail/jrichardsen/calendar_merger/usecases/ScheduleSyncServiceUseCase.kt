package com.gmail.jrichardsen.calendar_merger.usecases

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gmail.jrichardsen.calendar_merger.worker.SyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import javax.inject.Inject

class ScheduleSyncServiceUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    companion object {
        private const val TAG = "ScheduleSyncServiceUseCase"
    }

    operator fun invoke() {
        Log.d(TAG, "Scheduling sync service")
        // TODO: retrieve the repetition interval from somewhere
        val interval = Duration.ofMinutes(15)
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(interval).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "calendar_merger.sync",
            ExistingPeriodicWorkPolicy.UPDATE,
            syncRequest,
        )
    }
}