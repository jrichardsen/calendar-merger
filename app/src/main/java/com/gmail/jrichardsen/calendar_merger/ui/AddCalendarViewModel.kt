package com.gmail.jrichardsen.calendar_merger.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.gmail.jrichardsen.calendar_merger.usecases.AddMergedCalendarUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.GetDependencySelectionUseCase
import com.gmail.jrichardsen.calendar_merger.utils.toColor
import com.gmail.jrichardsen.calendar_merger.utils.toUiColor
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
    companion object {
        private const val TAG = "AddCalendarViewModel"
    }

    init {
        viewModelScope.launch {
            getDependencySelectionUseCase(null).collect { selection ->
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
        scope.launch {
            Log.v(TAG, "Adding merged calendar")
            if (!addMergedCalendarUseCase(
                    state.name,
                    state.color.toColor(),
                    inputIds
                )
            ) {
                Log.e(TAG, "Merged calendar could not be added")
            }
        }
    }
}