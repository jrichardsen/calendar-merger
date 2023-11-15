package com.gmail.jrichardsen.calendar_merger.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CalendarListScreen(
    uiState: CalendarListUiState,
    onSyncCalendars: () -> Unit,
    onDeleteCalendar: (calendarId: Long) -> Unit,
    onNavigateToAddCalendar: () -> Unit,
    onNavigateToEditCalendar: (calendarId: Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Calendars")
                },
                actions = {
                    IconButton(onClick = onSyncCalendars) {
                        Icon(Icons.Filled.Refresh, "Reload calendars")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddCalendar) {
                Icon(Icons.Filled.Add, "Add new calendar")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .padding(innerPadding)
        ) {
            items(uiState.calendars) { calendar ->
                ListItem(
                    headlineContent = {
                        Text(text = calendar.name)
                    },
                    // TODO: show dialog before deleting calendar
                    trailingContent = {
                        IconButton(onClick = { onDeleteCalendar(calendar.id) }) {
                            Icon(Icons.Filled.Delete, "Delete calendar")
                        }
                    },
                    modifier = Modifier
                        .clickable {
                            onNavigateToEditCalendar(calendar.id)
                        },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCalendarListScreen() {
    CalendarListScreen(
        uiState = CalendarListUiState(
            calendars = listOf(
                CalendarItemUiState(0, "one calendar"),
                CalendarItemUiState(1, "another calendar")
            )
        ),
        onSyncCalendars = {},
        onDeleteCalendar = {},
        onNavigateToAddCalendar = {},
        onNavigateToEditCalendar = {},
    )
}

