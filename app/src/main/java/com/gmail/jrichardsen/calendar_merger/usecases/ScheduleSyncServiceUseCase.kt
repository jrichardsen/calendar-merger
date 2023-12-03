package com.gmail.jrichardsen.calendar_merger.usecases

import android.util.Log
import com.gmail.jrichardsen.calendar_merger.data.SettingsRepository
import com.gmail.jrichardsen.calendar_merger.data.SyncManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ScheduleSyncServiceUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val syncManager: SyncManager,
) {
    companion object {
        private const val TAG = "ScheduleSyncServiceUseCase"
    }

    suspend operator fun invoke() {
        Log.d(TAG, "Scheduling sync service")
        val interval = settingsRepository.getSettings().first().syncInterval
        if (interval != null) {
            syncManager.scheduleSynchronization(interval)
        } else {
            syncManager.cancelSynchronization()
        }
    }
}