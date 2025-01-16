package com.example.binchecker.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.binchecker.data.database.entity.CardInfo
import com.example.binchecker.data.network.dto.BINCardDto
import com.example.binchecker.data.network.dto.toCardInfo
import com.example.binchecker.repository.BINDatabaseRepository
import com.example.binchecker.repository.BINNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val binNetRepository: BINNetworkRepository,
    private val binDBRepository: BINDatabaseRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    fun getCardFromApi(numberCard: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            binNetRepository.getCardBIN(numberCard)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.d("ERROR_API", e.message.toString())
                    _uiState.value = UIState.Error("Ошибка получения данных, проверьте интернет соединение")
                }
                .collect {
                    _uiState.value = UIState.SuccessCard(it.toCardInfo(numberCard.replace(" ", "").toInt()))
                }
        }
    }

    fun getAllCards() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            binDBRepository.getAllCards()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.d("DB_ERROR", e.message.toString())
                    _uiState.value = UIState.Error("Ошибка получения данных")
                }
                .collect {
                    _uiState.value = UIState.SuccessCards(it)
                }
        }
    }

    fun addCard(card: CardInfo) {
        viewModelScope.launch {
            binDBRepository.insertCard(card)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.d("DB_ERROR", e.message.toString())
                    _uiState.value = UIState.Error("Ошибка добавления карты")
                }
                .collect {
                    _uiState.value = UIState.SuccessCard(card)
                    getAllCards()
                }
        }
    }

}