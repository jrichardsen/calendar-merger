package com.gmail.jrichardsen.calendar_merger.usecases

import com.gmail.jrichardsen.calendar_merger.repositories.LocalCalendarRepository
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncEventsUseCase @Inject constructor(
    private val localCalendarRepository: LocalCalendarRepository,
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    suspend operator fun invoke() {
        val mergedCalendars = mergedCalendarRepository.getMergedCalendars().first()
        localCalendarRepository.sync(mergedCalendars)
    }
}