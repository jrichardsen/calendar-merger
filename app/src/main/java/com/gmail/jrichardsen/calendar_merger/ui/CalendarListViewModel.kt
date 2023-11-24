package com.gmail.jrichardsen.calendar_merger.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.jrichardsen.calendar_merger.usecases.GetMergedCalendarsUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.RemoveMergedCalendarUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.SyncCalendarsUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.SyncEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarListViewModel @Inject constructor(
    private val scope: CoroutineScope,
    getMergedCalendarsUseCase: GetMergedCalendarsUseCase,
    private val syncCalendarsUseCase: SyncCalendarsUseCase,
    private val syncEventsUseCase: SyncEventsUseCase,
    private val removeMergedCalendarUseCase: RemoveMergedCalendarUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CalendarListUiState> = MutableStateFlow(
        CalendarListUiState(emptyList())
    )

    val uiState: StateFlow<CalendarListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getMergedCalendarsUseCase()
                .map {
                    CalendarListUiState(
                        it.map { calendar ->
                            CalendarItemUiState(calendar.localCalendar.id, calendar.localCalendar.name)
                        })
                }.collect {
                    _uiState.value = it
                }
        }
    }

    fun syncCalendars() {
        scope.launch {
            syncCalendarsUseCase()
            syncEventsUseCase()
        }
    }

    fun deleteCalendar(id: Long) {
        scope.launch(Dispatchers.Main) {
            removeMergedCalendarUseCase(id)
        }
    }
}