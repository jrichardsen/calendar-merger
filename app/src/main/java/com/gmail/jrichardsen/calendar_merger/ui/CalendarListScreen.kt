package com.gmail.jrichardsen.calendar_merger.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CalendarListScreen(
    uiState: CalendarListUiState,
    onSync: (() -> Unit) -> Unit,
    onDeleteCalendar: (calendarId: Long) -> Unit,
    onNavigateToAddCalendar: () -> Unit,
    onNavigateToEditCalendar: (calendarId: Long) -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val deleteDialog = remember { mutableStateOf<Long?>(null) }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Merged Calendars")
                },
                actions = {
                    IconButton(onClick = {
                        onSync {
                            scope.launch {
                                snackBarHostState.showSnackbar("Calendars synchronized")
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Sync, "Synchronize merged calendars")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddCalendar) {
                Icon(Icons.Filled.Add, "Add new calendar")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->
        if (uiState.calendars.isNotEmpty()) {
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
        } else {
            Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "There are currently no merged calendars. You can create one by pressing the Add button in the bottom right corner.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(64.dp),
                    color = MaterialTheme.colorScheme.secondary
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
        onSync = {},
        onDeleteCalendar = {},
        onNavigateToAddCalendar = {},
        onNavigateToEditCalendar = {},
        onNavigateToSettings = {},
    )
}

@Preview
@Composable
private fun PreviewEmptyCalendarListScreen() {
    CalendarListScreen(
        uiState = CalendarListUiState(
            calendars = emptyList(),
        ),
        onSync = {},
        onDeleteCalendar = {},
        onNavigateToAddCalendar = {},
        onNavigateToEditCalendar = {},
        onNavigateToSettings = {},
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

