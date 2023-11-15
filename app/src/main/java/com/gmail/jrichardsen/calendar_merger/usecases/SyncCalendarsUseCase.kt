package com.gmail.jrichardsen.calendar_merger.usecases

import com.gmail.jrichardsen.calendar_merger.repositories.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import javax.inject.Inject

class SyncCalendarsUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    suspend operator fun invoke() {
        val localCalendars = localCalendarRepository.getLocalCalendars()
        mergedCalendarRepository.updateLocalCalendars(localCalendars)
    }
}