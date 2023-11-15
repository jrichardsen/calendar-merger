package com.gmail.jrichardsen.calendar_merger.usecases

import com.gmail.jrichardsen.calendar_merger.entities.MergedCalendar
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMergedCalendarsUseCase @Inject constructor(
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    operator fun invoke(): Flow<List<MergedCalendar>> {
        return mergedCalendarRepository.getMergedCalendars()
    }
}