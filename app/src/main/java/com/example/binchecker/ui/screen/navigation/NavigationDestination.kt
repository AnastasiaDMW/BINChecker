package com.example.binchecker.ui.screen.navigation

import com.example.binchecker.R

interface NavigationDestination {
    val route: String
    val titleRes: Int
}

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home
}

object HistoryDestination: NavigationDestination {
    override val route = "history"
    override val titleRes = R.string.history
}