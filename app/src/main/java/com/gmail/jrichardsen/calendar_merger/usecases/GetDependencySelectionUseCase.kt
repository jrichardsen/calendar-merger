package com.gmail.jrichardsen.calendar_merger.usecases

import com.gmail.jrichardsen.calendar_merger.entities.CalendarSelectionItem
import com.gmail.jrichardsen.calendar_merger.entities.LocalCalendar
import com.gmail.jrichardsen.calendar_merger.repositories.MergedCalendarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetDependencySelectionUseCase @Inject constructor(
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    operator fun invoke(id: Long?): Flow<List<CalendarSelectionItem>> {
        return mergedCalendarRepository.getDependencySelection(id)
    }
}