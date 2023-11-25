package com.gmail.jrichardsen.calendar_merger.usecases

import android.util.Log
import com.gmail.jrichardsen.calendar_merger.entities.MergedCalendar
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMergedCalendarsUseCase @Inject constructor(
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    companion object {
        private const val TAG = "GetMergedCalendarsUseCase"
    }

    operator fun invoke(): Flow<List<MergedCalendar>> {
        Log.v(TAG, "Retrieving merged calendar list")
        return mergedCalendarRepository.getMergedCalendars()
    }
}