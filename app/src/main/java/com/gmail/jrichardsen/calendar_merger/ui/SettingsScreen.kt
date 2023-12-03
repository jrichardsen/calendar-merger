package com.gmail.jrichardsen.calendar_merger.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    onSelectSyncIntervalOption: (SyncIntervalOption) -> Unit,
    onNavigateUp: () -> Unit,
) {
    var showSyncIntervalDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Settings")
            }, navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(Icons.Filled.ArrowBack, "Navigate back")
                }
            })
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            ListItem(
                headlineContent = {
                    Text("Synchronize merged calendars")
                },
                supportingContent = {
                    Text(stringResource(id = uiState.selectedSyncIntervalOption.name))
                },
                modifier = Modifier.clickable {
                    showSyncIntervalDialog = true
                }
            )
        }
    }
    if (showSyncIntervalDialog) {
        AlertDialog(
            onDismissRequest = { showSyncIntervalDialog = false },
        ) {
            Column {
                uiState.syncIntervalOptions.forEach { option ->
                    ListItem(
                        headlineContent = {
                            Text(stringResource(id = option.name))
                        },
                        modifier = Modifier.clickable {
                            onSelectSyncIntervalOption(option)
                            showSyncIntervalDialog = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    SettingsScreen(
        uiState = SettingsUiState(),
        onSelectSyncIntervalOption = {},
        onNavigateUp = {},
    )
}