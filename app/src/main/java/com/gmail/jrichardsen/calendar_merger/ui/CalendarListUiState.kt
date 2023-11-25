package com.gmail.jrichardsen.calendar_merger.ui

import androidx.compose.ui.graphics.Color

data class CalendarListUiState(
    val calendars: List<CalendarItemUiState>,
)

data class CalendarItemUiState(
    val id: Long,
    val name: String,
    val color: Color,
)
