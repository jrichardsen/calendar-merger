package com.gmail.jrichardsen.calendar_merger.usecases

import android.graphics.Color
import com.gmail.jrichardsen.calendar_merger.repositories.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import javax.inject.Inject

class UpdateMergedCalendarUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    suspend operator fun invoke(
        id: Long,
        name: String,
        color: Color,
        inputIds: List<Long>
    ): Boolean {
        val updated = localCalendarRepository.updateLocalCalendarName(id, name, color)
        if (updated) {
            mergedCalendarRepository.updateMergedCalendar(id, name, color, inputIds)
        }
        return updated
    }
}