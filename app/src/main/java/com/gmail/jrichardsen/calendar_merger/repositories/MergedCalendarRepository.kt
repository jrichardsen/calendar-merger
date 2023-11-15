package com.gmail.jrichardsen.calendar_merger.repositories

import android.util.Log
import com.gmail.jrichardsen.calendar_merger.database.CalendarDao
import com.gmail.jrichardsen.calendar_merger.database.CalendarEntity
import com.gmail.jrichardsen.calendar_merger.database.CalendarWithDependencies
import com.gmail.jrichardsen.calendar_merger.database.DependencySelection
import com.gmail.jrichardsen.calendar_merger.entities.CalendarSelectionItem
import com.gmail.jrichardsen.calendar_merger.entities.LocalCalendar
import com.gmail.jrichardsen.calendar_merger.entities.MergedCalendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MergedCalendarRepository @Inject constructor(
    private val calendarDao: CalendarDao,
) {
    suspend fun addCalendar(id: Long, name: String, inputIds: List<Long>) {
        calendarDao.insertCalendarWithDependencies(
            CalendarEntity(id, name, null), inputIds
        )
    }

    fun getMergedCalendars(): Flow<List<MergedCalendar>> {
        return calendarDao.getMergedCalendars().map {
            it.map(CalendarWithDependencies::toMergedCalendar)
        }
    }

    fun getCalendar(id: Long): Flow<LocalCalendar> {
        return calendarDao.getCalendarById(id).map(CalendarEntity::toLocalCalendar)
    }

    fun getDependencySelection(id: Long?): Flow<List<CalendarSelectionItem>> {
        Log.d(
            "Repo", "Retrieving dependencies"
        )
        return calendarDao.getDependencySelections(id).map {
            it.map(DependencySelection::toCalendarSelectionItem)
        }
    }

    suspend fun updateLocalCalendars(calendars: List<LocalCalendar>) {
        calendarDao.replaceCalendars(*calendars.map(LocalCalendar::toCalendarEntity).toTypedArray())
    }

    suspend fun updateMergedCalendar(id: Long, name: String, inputIds: List<Long>) {
        calendarDao.updateCalendar(CalendarEntity(id, name, null), inputIds)
    }

    suspend fun removeMergedCalendar(id: Long) {
        calendarDao.deleteCalendarAndDependencies(id)
    }
}

fun CalendarEntity.toLocalCalendar(): LocalCalendar {
    return LocalCalendar(
        id = this.id,
        name = this.name,
        ownerAccount = this.ownerAccount,
    )
}

fun LocalCalendar.toCalendarEntity(): CalendarEntity {
    return CalendarEntity(
        id = this.id,
        name = this.name,
        ownerAccount = this.ownerAccount,
    )
}

fun CalendarWithDependencies.toMergedCalendar(): MergedCalendar {
    return MergedCalendar(
        this.calendar.toLocalCalendar(),
        this.inputs.map(CalendarEntity::toLocalCalendar),
    )
}

fun DependencySelection.toCalendarSelectionItem(): CalendarSelectionItem {
    return CalendarSelectionItem(
        this.calendar.toLocalCalendar(),
        this.selected,
    )
}