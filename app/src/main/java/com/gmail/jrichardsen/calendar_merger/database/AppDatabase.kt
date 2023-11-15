package com.gmail.jrichardsen.calendar_merger.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CalendarEntity::class, CalendarDependency::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao
}