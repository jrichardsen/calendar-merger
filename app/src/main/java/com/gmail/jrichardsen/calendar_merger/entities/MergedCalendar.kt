package com.gmail.jrichardsen.calendar_merger.entities

data class MergedCalendar(
    val localCalendar: LocalCalendar,
    val inputs: List<LocalCalendar>,
)
