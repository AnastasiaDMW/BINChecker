package com.example.binchecker.ui.screen.history

import com.example.binchecker.data.database.entity.CardInfo

sealed class HistoryUIState {
    data object Loading: HistoryUIState()
    data class Error(val error: String): HistoryUIState()
    data class SuccessCards(val cards: List<CardInfo> = emptyList()): HistoryUIState()
}