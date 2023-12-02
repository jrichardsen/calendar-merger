package com.gmail.jrichardsen.calendar_merger.ui

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ListRoute = "list"

fun NavGraphBuilder.calendarListScreen(
    onNavigateToAddCalendar: () -> Unit,
    onNavigateToEditCalendar: (calendarId: Long) -> Unit
) {
    composable(ListRoute) {
        val viewModel: CalendarListViewModel = hiltViewModel()
        val uiState: CalendarListUiState by viewModel.uiState.collectAsStateWithLifecycle()
        CalendarListScreen(
            uiState,
            viewModel::sync,
            viewModel::deleteCalendar,
            onNavigateToAddCalendar,
            onNavigateToEditCalendar,
        )
    }
}

fun NavController.navigateToList(navOptions: NavOptions? = null) {
    this.navigate(ListRoute, navOptions)
}