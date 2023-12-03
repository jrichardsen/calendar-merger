package com.gmail.jrichardsen.calendar_merger.data

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gmail.jrichardsen.calendar_merger.worker.SyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import javax.inject.Inject

class SyncManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    companion object {
        private const val WORK_NAME = "calendar_merger.sync"
    }

    private val workManager = WorkManager.getInstance(context)

    fun scheduleSynchronization(interval: Duration) {
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(interval).build()
        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            syncRequest,
        )

    }

    fun cancelSynchronization() {
        workManager.cancelUniqueWork(WORK_NAME)
    }
}