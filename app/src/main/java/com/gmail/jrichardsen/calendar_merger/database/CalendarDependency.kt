package com.gmail.jrichardsen.calendar_merger.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "dependencies", primaryKeys = ["merged_id", "input_id"])
data class CalendarDependency(
    @ColumnInfo(name = "merged_id")
    val mergedId: Long,
    @ColumnInfo(name = "input_id", index = true)
    val inputId: Long,
)