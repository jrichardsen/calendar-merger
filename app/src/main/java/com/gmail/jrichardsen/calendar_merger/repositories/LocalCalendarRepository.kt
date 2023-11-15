package com.gmail.jrichardsen.calendar_merger.repositories

import android.content.ContentResolver
import android.content.Context
import android.provider.CalendarContract
import com.gmail.jrichardsen.calendar_merger.entities.LocalCalendar
import com.gmail.jrichardsen.calendar_merger.entities.MergedCalendar
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalCalendarRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun getLocalCalendars(): List<LocalCalendar> {
        return withContext(Dispatchers.IO) {
            val calendars = mutableListOf<LocalCalendar>()
            val contentResolver: ContentResolver = context.contentResolver

            val projection = arrayOf(
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE
            )

            val cursor = contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                null,
                null,
                null
            )

            cursor?.use {
                while (it.moveToNext()) {
                    val calendarId =
                        it.getLong(it.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
                    val calendarName =
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME))
                    val accountName =
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Calendars.ACCOUNT_NAME))
                    val accountType =
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Calendars.ACCOUNT_TYPE))
                    val account = if (accountType != CalendarContract.ACCOUNT_TYPE_LOCAL) {
                        accountName
                    } else {
                        null
                    }
                    val calendar = LocalCalendar(calendarId, calendarName, account)
                    calendars.add(calendar)
                }
            }

            return@withContext calendars
        }
    }

    suspend fun addLocalCalendar(): Long {
        // TODO: implement
        return 0
    }

    suspend fun updateLocalCalendarName(id: Long, name: String) {
        // TODO: implement
    }

    suspend fun removeLocalCalendar(id: Long) {
        // TODO: implement
    }

    suspend fun sync(mergedCalendars: List<MergedCalendar>) {
        // TODO: implement
    }
}

// fun getAllCalendars(context: Context): List<CalendarInfo> {
//     val calendars = mutableListOf<CalendarInfo>()
//     val contentResolver: ContentResolver = context.contentResolver
//
//     val projection = arrayOf(
//         CalendarContract.Calendars._ID,
//         CalendarContract.Calendars.NAME,
//         CalendarContract.Calendars.ACCOUNT_NAME,
//         CalendarContract.Calendars.ACCOUNT_TYPE
//     )
//
//     val cursor = contentResolver.query(
//         CalendarContract.Calendars.CONTENT_URI,
//         projection,
//         null,
//         null,
//         null
//     )
//
//     cursor?.use {
//         while (it.moveToNext()) {
//             val calendarId = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
//             val calendarName =
//                 it.getString(it.getColumnIndexOrThrow(CalendarContract.Calendars.NAME))
//             val accountName =
//                 it.getString(it.getColumnIndexOrThrow(CalendarContract.Calendars.ACCOUNT_NAME))
//             val accountType =
//                 it.getString(it.getColumnIndexOrThrow(CalendarContract.Calendars.ACCOUNT_TYPE))
//
//             val calendarInfo = CalendarInfo(calendarId, calendarName, accountName, accountType)
//             calendars.add(calendarInfo)
//         }
//     }
//
//     return calendars
// }
