package com.gmail.jrichardsen.calendar_merger.usecases

import android.graphics.Color
import android.util.Log
import com.gmail.jrichardsen.calendar_merger.data.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.data.MergedCalendarRepository
import javax.inject.Inject

class AddMergedCalendarUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    companion object {
        private const val TAG = "AddMergedCalenderUseCase"
    }

    suspend operator fun invoke(name: String, color: Color, inputIds: List<Long>): Long? {
        Log.v(TAG, "Adding new calendar")
        val id = localCalendarRepository.addLocalCalendar(name, color)
        if (id != null) {
            mergedCalendarRepository.addCalendar(id, name, color, inputIds)
        } else {
            Log.w(TAG, "Calendar was not added locally, omitting database update")
        }
        return id
    }
}