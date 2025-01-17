package com.example.binchecker.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.binchecker.repository.BINDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val binDBRepository: BINDatabaseRepository
): ViewModel() {

    private val _historyUiState: MutableStateFlow<HistoryUIState> = MutableStateFlow(HistoryUIState.Loading)
    val historyUiState: StateFlow<HistoryUIState> = _historyUiState

    fun getAllCards() {
        viewModelScope.launch {
            try {
                _historyUiState.value = HistoryUIState.Loading
                binDBRepository.getAllCards()
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        getAllCards()
                    }
                    .collect {
                        _historyUiState.value = HistoryUIState.SuccessCards(it)
                    }
            } catch (e: IOException) {
                _historyUiState.value = HistoryUIState.Error("Ошибка получения карт")
            }
        }
    }
}