package com.gmail.jrichardsen.calendar_merger.ui

import androidx.lifecycle.viewModelScope
import com.gmail.jrichardsen.calendar_merger.usecases.AddMergedCalendarUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.GetDependencySelectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class AddCalendarViewModel @Inject constructor(
    private val scope: CoroutineScope,
    private val getDependencySelectionUseCase: GetDependencySelectionUseCase,
    private val addMergedCalendarUseCase: AddMergedCalendarUseCase,
) : BaseEditCalendarViewModel() {

    init {
        viewModelScope.launch {
            getDependencySelectionUseCase(null).collect { selection ->
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
            addMergedCalendarUseCase(state.name, inputIds)
        }
    }
}