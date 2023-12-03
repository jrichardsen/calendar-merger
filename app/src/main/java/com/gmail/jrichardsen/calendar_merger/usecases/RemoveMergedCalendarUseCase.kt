package com.gmail.jrichardsen.calendar_merger.usecases

import android.util.Log
import com.gmail.jrichardsen.calendar_merger.data.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.data.MergedCalendarRepository
import javax.inject.Inject

class RemoveMergedCalendarUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    companion object {
        private const val TAG = "RemoveMergedCalendarUseCase"
    }

    suspend operator fun invoke(id: Long): Boolean {
        Log.v(TAG, "Removing merged calendar with id $id")
        val removed = localCalendarRepository.removeLocalCalendar(id)
        if (removed) {
            mergedCalendarRepository.removeMergedCalendar(id)
        }
        return removed
    }
}