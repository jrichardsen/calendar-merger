package com.gmail.jrichardsen.calendar_merger

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gmail.jrichardsen.calendar_merger.ui.ListRoute
import com.gmail.jrichardsen.calendar_merger.ui.addScreen
import com.gmail.jrichardsen.calendar_merger.ui.calendarListScreen
import com.gmail.jrichardsen.calendar_merger.ui.editScreen
import com.gmail.jrichardsen.calendar_merger.ui.navigateToAddScreen
import com.gmail.jrichardsen.calendar_merger.ui.navigateToEditScreen

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
    }
}