package com.gmail.jrichardsen.calendar_merger.data

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract
import android.provider.CalendarContract.Calendars
import android.provider.CalendarContract.Events
import android.util.Log
import com.gmail.jrichardsen.calendar_merger.entities.LocalCalendar
import com.gmail.jrichardsen.calendar_merger.entities.MergedCalendar
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalCalendarRepository @Inject constructor(
    @ApplicationContext context: Context,
) {
    companion object {
        private const val TAG = "LocalCalendarRepository"
    }

    private val contentResolver = context.contentResolver
    private val calendarUri = Calendars.CONTENT_URI.buildUpon()
        .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
        .appendQueryParameter(Calendars.ACCOUNT_NAME, "local")
        .appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
        .build()
    private val eventsUri = Events.CONTENT_URI.buildUpon()
        .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
        .appendQueryParameter(Calendars.ACCOUNT_NAME, "local")
        .appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
        .build()


    suspend fun getLocalCalendars(): List<LocalCalendar> {
        return withContext(Dispatchers.IO) {
            val calendars = mutableListOf<LocalCalendar>()

            val projection = arrayOf(
                Calendars._ID,
                Calendars.CALENDAR_DISPLAY_NAME,
                Calendars.CALENDAR_COLOR,
                Calendars.ACCOUNT_NAME,
                Calendars.ACCOUNT_TYPE,
            )

            val cursor = contentResolver.query(
                Calendars.CONTENT_URI,
                projection,
                null,
                null,
                null
            )

            cursor?.use {
                while (it.moveToNext()) {
                    val calendarId = it.getLong(0)
                    val calendarName = it.getString(1)
                    val color = Color.valueOf(it.getInt(2))
                    val accountName = it.getString(3)
                    val accountType = it.getString(4)
                    val account = if (accountType != CalendarContract.ACCOUNT_TYPE_LOCAL) {
                        accountName
                    } else {
                        null
                    }
                    val calendar = LocalCalendar(calendarId, calendarName, color, account)
                    calendars.add(calendar)
                }
            }

            calendars
        }
    }

    suspend fun addLocalCalendar(name: String, color: Color): Long? {
        return withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(Calendars.ACCOUNT_NAME, "local")
                put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                put(Calendars.NAME, "$name (merged)")
                put(Calendars.CALENDAR_DISPLAY_NAME, name)
                put(Calendars.CALENDAR_COLOR, color.toArgb())
                put(
                    Calendars.CALENDAR_ACCESS_LEVEL,
                    Calendars.CAL_ACCESS_READ,
                )
                put(Calendars.OWNER_ACCOUNT, "local")
            }

            val result = contentResolver.insert(calendarUri, values)

            val id = result?.lastPathSegment?.toLong()

            if (id != null) {
                Log.i(TAG, "Added new calendar with id $id")
            }

            id
        }
    }

    suspend fun updateLocalCalendarName(id: Long, name: String, color: Color): Boolean {
        return withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(Calendars.NAME, "$name (merged)")
                put(Calendars.CALENDAR_DISPLAY_NAME, name)
                put(Calendars.CALENDAR_COLOR, color.toArgb())
            }
            val uri = ContentUris.withAppendedId(Calendars.CONTENT_URI, id)

            val rowsUpdated = contentResolver.update(uri, values, null, null)
            Log.i(TAG, "Updated $rowsUpdated rows from calendar table")
            rowsUpdated > 0
        }
    }

    suspend fun removeLocalCalendar(id: Long): Boolean {
        return withContext(Dispatchers.IO) {
            val uri = ContentUris.withAppendedId(Calendars.CONTENT_URI, id)

            val rowsDeleted = contentResolver.delete(uri, null, null)
            Log.i(TAG, "Deleted $rowsDeleted rows from calendar table")
            rowsDeleted > 0
        }
    }

    suspend fun sync(mergedCalendars: List<MergedCalendar>) {
        withContext(Dispatchers.IO) {
            mergedCalendars.forEach { mergedCalendar ->
                val id = mergedCalendar.localCalendar.id
                clearEvents(id)
                mergedCalendar.inputs.forEach { input ->
                    copyEvents(input.id, id)
                }
            }
        }
    }

    private fun clearEvents(id: Long) {
        val selection = "${Events.CALENDAR_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val rowsDeleted = contentResolver.delete(eventsUri, selection, selectionArgs)
        Log.i(TAG, "Deleted $rowsDeleted rows from event table")
    }

    private fun copyEvents(from: Long, to: Long) {
        val columns = arrayOf(
            Events._ID,
            Events.CALENDAR_ID,
            Events.ORGANIZER,
            Events.TITLE,
            Events.EVENT_LOCATION,
            Events.DESCRIPTION,
            Events.EVENT_COLOR,
            Events.DTSTART,
            Events.DTEND,
            Events.EVENT_TIMEZONE,
            Events.EVENT_END_TIMEZONE,
            Events.DURATION,
            Events.ALL_DAY,
            Events.RRULE,
            Events.RDATE,
            Events.EXRULE,
            Events.EXDATE,
            Events.ORIGINAL_ID,
            Events.ORIGINAL_SYNC_ID,
            Events.ORIGINAL_INSTANCE_TIME,
            Events.ORIGINAL_ALL_DAY,
            Events.ACCESS_LEVEL,
            Events.AVAILABILITY,
            Events.GUESTS_CAN_MODIFY,
            Events.GUESTS_CAN_INVITE_OTHERS,
            Events.GUESTS_CAN_SEE_GUESTS,
            Events.CUSTOM_APP_PACKAGE,
            Events.CUSTOM_APP_URI,
            Events.UID_2445,
        )

        val selection = "${Events.CALENDAR_ID} = ?"
        val selectionArgs = arrayOf(from.toString())
        val cursor =
            contentResolver.query(Events.CONTENT_URI, columns, selection, selectionArgs, null)

        val events = ArrayList<ContentValues>(cursor?.count ?: 0)
        cursor?.use { c ->
            Log.i(TAG, "Copying ${c.count} events to calendar")
            while (c.moveToNext()) {
                val values = ContentValues().apply {
                    columns.mapIndexed { i, column ->
                        val value = when (column) {
                            Events._ID -> null
                            Events.CALENDAR_ID -> to.toString()
                            else -> c.getString(i)
                        }
                        value?.let {
                            put(column, it)
                        }
                    }
                    val originalId = c.getString(0)
                    put(Events.SYNC_DATA1, originalId)
                    put(Events.SYNC_DATA2, from)
                }
                events.add(values)
            }
        }

        // Insert events that are not exceptions
        val baseEvents = events.filter { it.containsKey(Events.ORIGINAL_ID).not() }
        contentResolver.bulkInsert(eventsUri, baseEvents.toTypedArray())

        // Retrieve the ids of the base events and build an id translation map
        val idCursor = contentResolver.query(
            eventsUri,
            arrayOf(Events._ID, Events.SYNC_DATA1),
            "(${Events.CALENDAR_ID} == ?) AND (${Events.SYNC_DATA2} == ?)",
            arrayOf(to.toString(), from.toString()),
            null
        )
        val idMap = HashMap<String, String>()
        idCursor?.use {
            while (it.moveToNext()) {
                idMap[it.getString(1)] = it.getString(0)
            }
        }

        // Update the ids and insert them into the database
        val exceptions = events.filter { it.containsKey(Events.ORIGINAL_ID) }
        exceptions.forEach { it.put(Events.ORIGINAL_ID, idMap[it.getAsString(Events.ORIGINAL_ID)]) }
        contentResolver.bulkInsert(eventsUri, exceptions.toTypedArray())
    }
}