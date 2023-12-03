package com.gmail.jrichardsen.calendar_merger.data

import androidx.room.Embedded

data class DependencySelection(
    @Embedded
    val calendar: CalendarEntity,
    val selected: Boolean,
)
