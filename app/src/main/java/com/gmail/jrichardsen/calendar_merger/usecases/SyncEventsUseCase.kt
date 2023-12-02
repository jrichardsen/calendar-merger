package com.gmail.jrichardsen.calendar_merger.usecases

import android.util.Log
import com.gmail.jrichardsen.calendar_merger.repositories.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncEventsUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
    private val syncCalendarsUseCase: SyncCalendarsUseCase,
) {
    companion object {
        private const val TAG = "SyncEventsUseCase"
    }

    suspend operator fun invoke(id: Long? = null) {
        syncCalendarsUseCase()
        val mergedCalendars = if (id == null) {
            Log.v(TAG, "Started syncing events")
            mergedCalendarRepository.getMergedCalendars().first()
        } else {
            Log.v(TAG, "Started syncing events for calendar $id")
            listOf(mergedCalendarRepository.getMergedCalendar(id).first())
        }
        localCalendarRepository.sync(mergedCalendars)
        Log.v(TAG, "Syncing events completed")
    }
}