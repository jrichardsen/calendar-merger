package com.gmail.jrichardsen.calendar_merger.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CalendarDao {
    @Insert
    abstract suspend fun insertCalendars(vararg calendars: CalendarEntity)

    @Insert
    abstract suspend fun insertDependencies(vararg dependencies: CalendarDependency)

    @Query("SELECT * FROM calendars WHERE id=:id")
    abstract fun getCalendarById(id: Long): Flow<CalendarEntity>

    @Query(
        "SELECT *, CAST((SELECT COUNT(*) FROM dependencies WHERE merged_id=:id AND input_id=d.id) AS bit) AS selected " +
                "FROM calendars d " +
                "WHERE d.id NOT IN (SELECT merged_id FROM dependencies)"
    )
    abstract fun getDependencySelections(id: Long?): Flow<List<DependencySelection>>

    @Transaction
    @Query("SELECT * FROM calendars WHERE id=:id")
    abstract fun getMergedCalendar(id: Long): Flow<CalendarWithDependencies>

    @Transaction
    @Query(
        "SELECT * " +
                "FROM calendars " +
                "WHERE id IN (SELECT merged_id FROM dependencies)"
    )
    abstract fun getMergedCalendars(): Flow<List<CalendarWithDependencies>>

    @Query(
        "DELETE FROM dependencies " +
                "WHERE merged_id NOT IN (SELECT id FROM calendars) " +
                "OR input_id NOT IN (SELECT id FROM calendars)"
    )
    abstract suspend fun cleanupDependencies()

    @Query("DELETE FROM calendars")
    protected abstract suspend fun deleteAllCalendars()

    @Query("DELETE FROM calendars WHERE id=:id")
    protected abstract suspend fun deleteCalendar(id: Long)

    @Query("DELETE FROM dependencies WHERE merged_id=:id OR input_id=:id")
    protected abstract suspend fun deleteDependenciesFor(id: Long)

    @Transaction
    open suspend fun insertCalendarWithDependencies(calendar: CalendarEntity, dependencies: List<Long>) {
        insertCalendars(calendar)
        insertDependencies(*(dependencies.map {
            CalendarDependency(calendar.id, it)
        }.toTypedArray()))
    }

    @Transaction
    open suspend fun replaceCalendars(vararg calendars: CalendarEntity) {
        deleteAllCalendars()
        insertCalendars(*calendars)
        cleanupDependencies()
    }

    @Transaction
    open suspend fun updateCalendar(calendar: CalendarEntity, dependencies: List<Long>) {
        deleteCalendarAndDependencies(calendar.id)
        insertCalendarWithDependencies(calendar, dependencies)
    }

    @Transaction
    open suspend fun deleteCalendarAndDependencies(id: Long) {
        deleteCalendar(id)
        deleteDependenciesFor(id)
    }
}