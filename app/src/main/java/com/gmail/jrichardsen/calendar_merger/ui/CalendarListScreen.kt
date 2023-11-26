package com.gmail.jrichardsen.calendar_merger.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CalendarListScreen(
    uiState: CalendarListUiState,
    onSyncCalendars: () -> Unit,
    onDeleteCalendar: (calendarId: Long) -> Unit,
    onNavigateToAddCalendar: () -> Unit,
    onNavigateToEditCalendar: (calendarId: Long) -> Unit,
) {
    val deleteDialog = remember { mutableStateOf<Long?>(null) }
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
                    leadingContent = {
                        Box(
                            Modifier
                                .width(8.dp)
                                .height(48.dp)
                                .background(calendar.color)
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = {
                            deleteDialog.value = calendar.id
                        }) {
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
    deleteDialog.value?.let {
        val onDismiss = { deleteDialog.value = null }
        val onDelete = {
            onDeleteCalendar(it)
            onDismiss()
        }
        DeleteDialog(onDismiss, onDelete)
    }
}

@Composable
private fun DeleteDialog(onDismiss: () -> Unit, onDelete: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = "Delete calendar?")
        },
        text = {
            Text(text = "The calendar, including all its events, will be deleted from your system. The calendars it was merged from will not change.")
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onDelete
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
private fun PreviewCalendarListScreen() {
    CalendarListScreen(
        uiState = CalendarListUiState(
            calendars = listOf(
                CalendarItemUiState(0, "one calendar", Color.Red),
                CalendarItemUiState(1, "another calendar", Color.Blue)
            )
        ),
        onSyncCalendars = {},
        onDeleteCalendar = {},
        onNavigateToAddCalendar = {},
        onNavigateToEditCalendar = {},
    )
}

@Preview
@Composable
private fun PreviewDeleteDialog() {
    DeleteDialog(
        onDismiss = {},
        onDelete = {},
    )
}

