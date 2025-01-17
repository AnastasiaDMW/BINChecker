package com.example.binchecker.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.binchecker.ui.screen.history.HistoryScreen
import com.example.binchecker.ui.screen.history.HistoryViewModel
import com.example.binchecker.ui.screen.home.HomeScreen
import com.example.binchecker.ui.screen.home.HomeViewModel

@Composable
fun BINCheckerNavHost(
    homeViewModel: HomeViewModel,
    historyViewModel: HistoryViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                navigateToHistoryScreen = {
                    navController.navigate(HistoryDestination.route)
                }
            )
        }
        composable(route = HistoryDestination.route) {
            HistoryScreen(historyViewModel = historyViewModel)
        }
    }
}