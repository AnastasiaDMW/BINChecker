package com.example.binchecker.ui.screen.home

import com.example.binchecker.data.database.entity.CardInfo

sealed class UIState {
    data object Loading: UIState()
    data class Error(val error: String): UIState()
    data class SuccessCard(val curCard: CardInfo? = null): UIState()
}