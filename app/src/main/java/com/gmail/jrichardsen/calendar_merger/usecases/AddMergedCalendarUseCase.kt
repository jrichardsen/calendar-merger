package com.gmail.jrichardsen.calendar_merger.usecases

import com.gmail.jrichardsen.calendar_merger.repositories.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import javax.inject.Inject

class AddMergedCalendarUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    suspend operator fun invoke(name: String, inputIds: List<Long>): Boolean {
        val id = localCalendarRepository.addLocalCalendar(name)
        id?.let {
            mergedCalendarRepository.addCalendar(id, name, inputIds)
        }
        return id != null
    }
}