package com.gmail.jrichardsen.calendar_merger.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.gmail.jrichardsen.calendar_merger.usecases.GetCalendarUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.GetDependencySelectionUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.UpdateMergedCalendarUseCase
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
) : BaseEditCalendarViewModel() {

    private val editArgs = EditArgs(savedStateHandle)

    init {
        viewModelScope.launch {
            getCalendarUseCase(editArgs.calendarId).collect { calendar ->
                mutUiState.update {
                    it.copy(id = calendar.id, name = calendar.name)
                }
            }
        }
        viewModelScope.launch {
            getDependencySelectionUseCase(editArgs.calendarId).collect { selection ->
                mutUiState.update {
                    it.copy(calendarSelection = selection)
                }
            }
        }
    }

    override fun save() {
        val state = uiState.value
        val inputIds = state.calendarSelection.stream().filter {
            it.selected
        }.map {
            it.calendar.id
        }.collect(Collectors.toList())
        scope.launch {
            updateMergedCalendarUseCase(editArgs.calendarId, state.name, inputIds)
        }
    }
}