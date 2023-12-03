package com.gmail.jrichardsen.calendar_merger.usecases

import android.util.Log
import com.gmail.jrichardsen.calendar_merger.data.MergedCalendarRepository
import com.gmail.jrichardsen.calendar_merger.entities.LocalCalendar
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCalendarUseCase @Inject constructor(
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    companion object {
        private const val TAG = "GetCalenderUseCase"
    }

    operator fun invoke(id: Long): Flow<LocalCalendar> {
        Log.v(TAG, "Retrieving local calender with id $id")
        return mergedCalendarRepository.getCalendar(id)
    }
}