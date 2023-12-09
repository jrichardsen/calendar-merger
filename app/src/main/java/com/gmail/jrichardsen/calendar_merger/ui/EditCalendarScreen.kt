package com.gmail.jrichardsen.calendar_merger.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmail.jrichardsen.calendar_merger.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditCalendarScreen(
    uiState: EditCalendarUiState,
    onChangeName: (String) -> Unit,
    onChangeColor: (String) -> Unit,
    onChangeSelection: (Int, Boolean) -> Unit,
    onNavigateUp: () -> Unit,
    onSave: () -> Unit,
) {
    val discardDialog = remember { mutableStateOf(false) }
    val navigateUp = {
        if (uiState.dirty) {
            discardDialog.value = true
        } else {
            onNavigateUp()
        }
    }
    BackHandler { navigateUp() }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    stringResource(
                        if (uiState.id == null) R.string.new_calendar else
                            R.string.edit_calendar
                    )
                )
            }, navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.Filled.ArrowBack, stringResource(R.string.navigate_back))
                }
            })
        },
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
        ) {
            TextField(
                value = uiState.name, onValueChange = onChangeName, label = {
                    Text(stringResource(R.string.name))
                },
                singleLine = true, modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .fillMaxWidth()
            )
            TextField(
                value = uiState.colorInput, onValueChange = onChangeColor, label = {
                    Text(stringResource(R.string.color))
                }, trailingIcon = {
                    Box(
                        Modifier
                            .size(24.dp)
                            .background(uiState.color, CircleShape)
                    )
                },
                singleLine = true, modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = stringResource(R.string.calendars_to_merge),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp, 8.dp)
            )
            LazyColumn(
                Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            ) {
                itemsIndexed(uiState.calendarSelection) { index, it ->
                    ListItem(
                        headlineContent = {
                            Text(it.name)
                        },
                        leadingContent = {
                            Box(
                                Modifier
                                    .width(8.dp)
                                    .height(48.dp)
                                    .background(it.color)
                            )
                        },
                        supportingContent = {
                            Text(it.ownerAccount ?: stringResource(R.string.local))
                        },
                        trailingContent = {
                            Checkbox(checked = it.selected, onCheckedChange = {
                                onChangeSelection(index, it)
                            })
                        }
                    )
                }
            }
            Button(
                content = { Text(stringResource(R.string.save)) },
                enabled = uiState.canSave,
                onClick = { onSave() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
    if (discardDialog.value) {
        val onDismiss = { discardDialog.value = false }
        val onConfirm = {
            onNavigateUp()
            onDismiss()
        }
        DiscardChangesDialog(
            onDismiss = onDismiss,
            onConfirm = onConfirm,
        )
    }
}

@Composable
private fun DiscardChangesDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.discard_dialog_title))
        },
        text = {
            Text(text = stringResource(R.string.discard_dialog_text))
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
private fun PreviewEditCalendarScreen() {
    EditCalendarScreen(
        uiState = EditCalendarUiState(
            id = 0,
            name = "My merged calendar",
            colorInput = "teal",
            calendarSelection = listOf(
                CalendarSelectionItemState(
                    0,
                    "Work",
                    Color.Red,
                    "my@company.com",
                    true
                ),
                CalendarSelectionItemState(
                    1,
                    "Private",
                    Color.Blue,
                    "example@gmail.com",
                    false
                ),
                CalendarSelectionItemState(
                    2,
                    "Sports",
                    Color.Cyan,
                    null,
                    true
                )
            )
        ),
        onChangeName = {},
        onChangeColor = {},
        onChangeSelection = { _, _ -> },
        onNavigateUp = {},
        onSave = {},
    )
}

@Preview
@Composable
private fun PreviewDiscardChangesDialog() {
    DiscardChangesDialog(
        onDismiss = {},
        onConfirm = {},
    )
}