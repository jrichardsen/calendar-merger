package com.gmail.jrichardsen.calendar_merger.ui

import com.gmail.jrichardsen.calendar_merger.entities.CalendarSelectionItem

data class EditCalendarUiState(
    val id: Long?,
    val name: String,
    val calendarSelection: List<CalendarSelectionItem>,
) {
    val canSave = name.isNotEmpty() && calendarSelection.any(CalendarSelectionItem::selected)
}