package com.gmail.jrichardsen.calendar_merger.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val calendarIdArg = "calendarId"

fun NavGraphBuilder.addScreen(
    onNavigateUp: () -> Unit
) {
    composable("add") {
        val viewModel: AddCalendarViewModel = hiltViewModel()
        EditScreen(onNavigateUp, viewModel)
    }
}

fun NavGraphBuilder.editScreen(
    onNavigateUp: () -> Unit
) {
    composable(
        "edit/{$calendarIdArg}",
        listOf(navArgument(calendarIdArg) { type = NavType.LongType })
    ) {
        val viewModel: EditCalendarViewModel = hiltViewModel()
        EditScreen(onNavigateUp, viewModel)
    }
}

@Composable
private fun EditScreen(
    onNavigateUp: () -> Unit,
    viewModel: BaseEditCalendarViewModel,
) {
    val uiState: EditCalendarUiState by viewModel.uiState.collectAsStateWithLifecycle()

    EditCalendarScreen(
        uiState = uiState,
        onChangeName = viewModel::updateName,
        onChangeSelection = viewModel::updateSelection,
        onNavigateUp = onNavigateUp,
        onSave = {
            viewModel.save()
            onNavigateUp()
        }
    )
}

fun NavController.navigateToAddScreen(navOptions: NavOptions? = null) {
    this.navigate("add", navOptions)
}

fun NavController.navigateToEditScreen(calendarId: Long, navOptions: NavOptions? = null) {
    this.navigate("edit/$calendarId", navOptions)
}

internal class EditArgs(val calendarId: Long) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(savedStateHandle.get<Long>(calendarIdArg)!!)
}