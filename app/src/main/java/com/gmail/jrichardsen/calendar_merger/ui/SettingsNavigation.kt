package com.gmail.jrichardsen.calendar_merger.ui

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SettingsRoute = "settings"

fun NavGraphBuilder.settingsScreen(
    onNavigateUp: () -> Unit,
) {
    composable(SettingsRoute) {
        val viewModel: SettingsViewModel = hiltViewModel()
        val uiState: SettingsUiState by viewModel.uiState.collectAsStateWithLifecycle()
        SettingsScreen(
            uiState = uiState,
            onSelectSyncIntervalOption = viewModel::changeSyncInterval,
            onNavigateUp = onNavigateUp,
        )
    }
}

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(SettingsRoute, navOptions)
}
