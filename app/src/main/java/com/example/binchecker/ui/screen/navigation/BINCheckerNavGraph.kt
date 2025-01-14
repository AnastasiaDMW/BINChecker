package com.example.binchecker.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.binchecker.ui.screen.history.HistoryScreen
import com.example.binchecker.ui.screen.home.HomeScreen

@Composable
fun BINCheckerNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen()
        }
        composable(route = HistoryDestination.route) {
            HistoryScreen()
        }
    }
}