package com.gmail.jrichardsen.calendar_merger.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseEditCalendarViewModel : ViewModel() {

    protected val mutUiState: MutableStateFlow<EditCalendarUiState> = MutableStateFlow(
        EditCalendarUiState(
            id = null,
            name = "",
            colorInput = "red",
            calendarSelection = emptyList()
        )
    )

    val uiState = mutUiState.asStateFlow()

    fun updateName(name: String) {
        mutUiState.update {
            it.copy(name = name)
        }
    }

    fun updateColor(color: String) {
        mutUiState.update {
            it.copy(colorInput = color)
        }
    }

    fun updateSelection(index: Int, selected: Boolean) {
        mutUiState.update {
            val newSelection = ArrayList(it.calendarSelection)
            newSelection[index] = newSelection[index].copy(selected = selected)
            it.copy(calendarSelection = newSelection)
        }
    }

    abstract fun save()
}