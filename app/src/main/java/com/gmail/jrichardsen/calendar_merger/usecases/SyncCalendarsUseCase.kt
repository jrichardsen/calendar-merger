package com.gmail.jrichardsen.calendar_merger.usecases

import android.util.Log
import com.gmail.jrichardsen.calendar_merger.data.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.data.MergedCalendarRepository
import javax.inject.Inject

class SyncCalendarsUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    companion object {
        private const val TAG = "SyncCalendarsUseCase"
    }

    suspend operator fun invoke() {
        Log.v(TAG, "Syncing local calendars")
        val localCalendars = localCalendarRepository.getLocalCalendars()
        mergedCalendarRepository.updateLocalCalendars(localCalendars)
    }
}