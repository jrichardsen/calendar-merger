package com.gmail.jrichardsen.calendar_merger.usecases

import com.gmail.jrichardsen.calendar_merger.repositories.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import javax.inject.Inject

class RemoveMergedCalendarUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    suspend operator fun invoke(id: Long): Boolean {
        val removed = localCalendarRepository.removeLocalCalendar(id)
        if (removed) {
            mergedCalendarRepository.removeMergedCalendar(id)
        }
        return removed
    }
}