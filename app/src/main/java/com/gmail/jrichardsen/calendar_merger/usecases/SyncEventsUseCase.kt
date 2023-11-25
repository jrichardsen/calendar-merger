package com.gmail.jrichardsen.calendar_merger.usecases

import android.util.Log
import com.gmail.jrichardsen.calendar_merger.repositories.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncEventsUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    companion object {
        private const val TAG = "SyncEventsUseCase"
    }

    suspend operator fun invoke() {
        Log.v(TAG, "Started syncing events")
        val mergedCalendars = mergedCalendarRepository.getMergedCalendars().first()
        localCalendarRepository.sync(mergedCalendars)
        Log.v(TAG, "Syncing events completed")
    }
}