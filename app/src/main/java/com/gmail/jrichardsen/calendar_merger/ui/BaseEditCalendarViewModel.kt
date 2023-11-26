package com.gmail.jrichardsen.calendar_merger.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseEditCalendarViewModel : ViewModel() {
    companion object {
        private const val TAG = "BaseEditCalendarViewModel"
    }

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
        Log.v(TAG, "Updating name to $name")
        mutUiState.update {
            it.copy(name = name, dirty = true)
        }
    }

    fun updateColor(color: String) {
        Log.v(TAG, "Updating color to $color")
        mutUiState.update {
            it.copy(colorInput = color, dirty = true)
        }
    }

    fun updateSelection(index: Int, selected: Boolean) {
        Log.v(
            TAG,
            "Updating selection, ${if (selected) "selecting" else "deselecting"} calendar at index $index"
        )
        mutUiState.update {
            val newSelection = ArrayList(it.calendarSelection)
            newSelection[index] = newSelection[index].copy(selected = selected)
            it.copy(calendarSelection = newSelection, dirty = true)
        }
    }

    abstract fun save()
}