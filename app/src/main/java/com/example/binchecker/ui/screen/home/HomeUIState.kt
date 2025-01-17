package com.example.binchecker.ui.screen.home

import com.example.binchecker.data.database.entity.CardInfo

sealed class HomeUIState {
    data object Loading: HomeUIState()
    data class Error(val error: String): HomeUIState()
    data class SuccessCard(val curCard: CardInfo? = null): HomeUIState()
}