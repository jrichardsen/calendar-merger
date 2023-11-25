package com.gmail.jrichardsen.calendar_merger.ui

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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(if (uiState.id == null) "New calendar" else "Edit calendar")
            }, navigationIcon = {
                //TODO: if there are unsaved changes, prompt before going back
                IconButton(onClick = onNavigateUp) {
                    Icon(Icons.Filled.ArrowBack, "Navigate back")
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
                    Text("Name")
                },
                singleLine = true, modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .fillMaxWidth()
            )
            TextField(
                value = uiState.colorInput, onValueChange = onChangeColor, label = {
                    Text("Color")
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
                text = "Calendars to merge",
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
                            Text(it.ownerAccount ?: "local")
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
                content = { Text("Save") },
                enabled = uiState.canSave,
                onClick = { onSave() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
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