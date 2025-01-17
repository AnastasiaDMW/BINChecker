package com.example.binchecker

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.binchecker.ui.screen.history.HistoryViewModel
import com.example.binchecker.ui.screen.home.HomeViewModel
import com.example.binchecker.ui.screen.navigation.BINCheckerNavHost

@Composable
fun BINCheckerApp(
    homeViewModel: HomeViewModel,
    historyViewModel: HistoryViewModel,
    navController: NavHostController = rememberNavController()
) {
    BINCheckerNavHost(
        homeViewModel,
        historyViewModel,
        navController
    )
}