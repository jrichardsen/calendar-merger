package com.gmail.jrichardsen.calendar_merger.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gmail.jrichardsen.calendar_merger.usecases.SyncEventsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val syncEventsUseCase: SyncEventsUseCase,
) : CoroutineWorker(applicationContext, workerParameters) {

    companion object {
        private const val TAG = "SyncWorker"
    }

    override suspend fun doWork(): Result {
        Log.v(TAG, "Running sync")
        syncEventsUseCase()
        Log.v(TAG, "Sync completed")
        return Result.success()
    }
}