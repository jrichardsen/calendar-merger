package com.gmail.jrichardsen.calendar_merger.usecases

import com.gmail.jrichardsen.calendar_merger.repositories.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import javax.inject.Inject

class UpdateMergedCalendarUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    suspend operator fun invoke(id: Long, name: String, inputIds: List<Long>) {
        localCalendarRepository.updateLocalCalendarName(id, name)
        mergedCalendarRepository.updateMergedCalendar(id, name, inputIds)
    }
}