package com.gmail.jrichardsen.calendar_merger.database

import androidx.room.Embedded

data class DependencySelection(
    @Embedded
    val calendar: CalendarEntity,
    val selected: Boolean,
)
