package com.gmail.jrichardsen.calendar_merger.usecases

import android.graphics.Color
import android.util.Log
import com.gmail.jrichardsen.calendar_merger.data.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.data.MergedCalendarRepository
import javax.inject.Inject

class UpdateMergedCalendarUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    companion object {
        private const val TAG = "UpdateMergedCalendarUseCase"
    }

    suspend operator fun invoke(
        id: Long,
        name: String,
        color: Color,
        inputIds: List<Long>
    ): Boolean {
        Log.v(TAG, "Updating merged calendar with id $id")
        val updated = localCalendarRepository.updateLocalCalendarName(id, name, color)
        if (updated) {
            mergedCalendarRepository.updateMergedCalendar(id, name, color, inputIds)
        } else {
            Log.w(TAG, "Calendar with $id was not updated locally, omitting database update")
        }
        return updated
    }
}