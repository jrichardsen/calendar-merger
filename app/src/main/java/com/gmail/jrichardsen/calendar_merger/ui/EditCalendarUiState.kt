package com.gmail.jrichardsen.calendar_merger.ui

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

data class EditCalendarUiState(
    val id: Long?,
    val name: String,
    val colorInput: String,
    val calendarSelection: List<CalendarSelectionItemState>,
) {
    val canSave = name.isNotEmpty() && calendarSelection.any(CalendarSelectionItemState::selected)
    val color = try {
        Color(colorInput.toColorInt())
    } catch (e: Exception) {
        Color.Black
    }
}

data class CalendarSelectionItemState(
    val id: Long,
    val name: String,
    val color: Color,
    val ownerAccount: String?,
    val selected: Boolean,
)
