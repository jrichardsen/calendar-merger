package com.gmail.jrichardsen.calendar_merger.data

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendars")
data class CalendarEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    @ColorInt
    val color: Int,
    @ColumnInfo(name = "owner_account")
    val ownerAccount: String?,
)