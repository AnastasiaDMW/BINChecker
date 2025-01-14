package com.example.binchecker

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.binchecker.ui.screen.navigation.BINCheckerNavHost

@Composable
fun BINCheckerApp(
    navController: NavHostController = rememberNavController()
) {
    BINCheckerNavHost(navController)
}