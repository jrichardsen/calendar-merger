package com.gmail.jrichardsen.calendar_merger.usecases

import android.util.Log
import com.gmail.jrichardsen.calendar_merger.data.MergedCalendarRepository
import com.gmail.jrichardsen.calendar_merger.entities.CalendarSelectionItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDependencySelectionUseCase @Inject constructor(
    private val mergedCalendarRepository: MergedCalendarRepository,
) {
    companion object {
        private const val TAG = "GetDependencySelectionUseCase"
    }

    operator fun invoke(id: Long?): Flow<List<CalendarSelectionItem>> {
        Log.v(TAG, "Retrieving dependency selection for id $id")
        return mergedCalendarRepository.getDependencySelection(id)
    }
}