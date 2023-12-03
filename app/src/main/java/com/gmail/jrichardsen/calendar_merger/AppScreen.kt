package com.gmail.jrichardsen.calendar_merger

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gmail.jrichardsen.calendar_merger.ui.ListRoute
import com.gmail.jrichardsen.calendar_merger.ui.addScreen
import com.gmail.jrichardsen.calendar_merger.ui.calendarListScreen
import com.gmail.jrichardsen.calendar_merger.ui.editScreen
import com.gmail.jrichardsen.calendar_merger.ui.navigateToAddScreen
import com.gmail.jrichardsen.calendar_merger.ui.navigateToEditScreen
import com.gmail.jrichardsen.calendar_merger.ui.navigateToSettings
import com.gmail.jrichardsen.calendar_merger.ui.settingsScreen

@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController, startDestination = ListRoute) {
        calendarListScreen(
            onNavigateToAddCalendar = {
                navController.navigateToAddScreen()
            },
            onNavigateToEditCalendar = {
                navController.navigateToEditScreen(it)
            },
            onNavigateToSettings = {
                navController.navigateToSettings()
            }
        )
        addScreen(
            onNavigateUp = {
                navController.navigateUp()
            }
        )
        editScreen(
            onNavigateUp = {
                navController.navigateUp()
            }
        )
        settingsScreen(
            onNavigateUp = {
                navController.navigateUp()
            }
        )
    }
}