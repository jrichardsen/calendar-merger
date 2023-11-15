package com.gmail.jrichardsen.calendar_merger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendars")
data class CalendarEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    @ColumnInfo(name = "owner_account")
    val ownerAccount: String?,
)