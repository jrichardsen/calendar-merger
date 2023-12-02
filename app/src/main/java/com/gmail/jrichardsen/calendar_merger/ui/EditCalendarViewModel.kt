package com.gmail.jrichardsen.calendar_merger.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.gmail.jrichardsen.calendar_merger.usecases.GetCalendarUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.GetDependencySelectionUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.SyncEventsUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.UpdateMergedCalendarUseCase
import com.gmail.jrichardsen.calendar_merger.utils.toColor
import com.gmail.jrichardsen.calendar_merger.utils.toUiColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class EditCalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val scope: CoroutineScope,
    private val getCalendarUseCase: GetCalendarUseCase,
    private val getDependencySelectionUseCase: GetDependencySelectionUseCase,
    private val updateMergedCalendarUseCase: UpdateMergedCalendarUseCase,
    private val syncEventsUseCase: SyncEventsUseCase,
) : BaseEditCalendarViewModel() {
    companion object {
        private const val TAG = "EditCalendarViewModel"
    }

    private val editArgs = EditArgs(savedStateHandle)

    init {
        viewModelScope.launch {
            getCalendarUseCase(editArgs.calendarId).collect { calendar ->
                mutUiState.update {
                    it.copy(
                        id = calendar.id,
                        name = calendar.name,
                        colorInput = "#%X".format(calendar.color.toArgb())
                    )
                }
            }
        }
        viewModelScope.launch {
            getDependencySelectionUseCase(editArgs.calendarId).collect { selection ->
                Log.v(TAG, "Updating dependency selection in UI")
                mutUiState.update { state ->
                    state.copy(calendarSelection = selection.map {
                        CalendarSelectionItemState(
                            it.calendar.id,
                            it.calendar.name,
                            it.calendar.color.toUiColor(),
                            it.calendar.ownerAccount,
                            it.selected,
                        )
                    })
                }
            }
        }
    }

    override fun save() {
        val state = uiState.value
        val inputIds = state.calendarSelection.stream().filter {
            it.selected
        }.map {
            it.id
        }.collect(Collectors.toList())
        mutUiState.update { it.copy(dirty = false) }
        scope.launch {
            Log.v(TAG, "Updating merged calendar")
            if (updateMergedCalendarUseCase(
                    editArgs.calendarId,
                    state.name,
                    state.color.toColor(),
                    inputIds
                )
            ) {
                syncEventsUseCase(editArgs.calendarId)
            } else {
                Log.e(TAG, "Merged calendar could not be updated")
            }
        }
    }
}