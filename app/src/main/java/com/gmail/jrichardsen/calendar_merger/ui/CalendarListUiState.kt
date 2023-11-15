package com.gmail.jrichardsen.calendar_merger.ui

data class CalendarListUiState(
    val calendars: List<CalendarItemUiState>,
)

data class CalendarItemUiState(
    val id: Long,
    val name: String,
)
