package com.gmail.jrichardsen.calendar_merger.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CalendarWithDependencies(
    @Embedded
    val calendar: CalendarEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            CalendarDependency::class,
            parentColumn = "merged_id",
            entityColumn = "input_id"
        ),
    )
    val inputs: List<CalendarEntity>
)
