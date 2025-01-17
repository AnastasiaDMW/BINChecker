package com.example.binchecker.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.binchecker.data.database.entity.CardInfo
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
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val binNetRepository: BINNetworkRepository,
    private val binDBRepository: BINDatabaseRepository
): ViewModel() {

    var isSendRequest = false

    private val _Home_uiState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState.SuccessCard(null))
    val homeUiState: StateFlow<HomeUIState> = _Home_uiState

    private val _cards: MutableStateFlow<List<CardInfo>> = MutableStateFlow(emptyList())
    val cards: StateFlow<List<CardInfo>> = _cards

    init {
        getAllCards()
    }

    private fun getCardFromApi(numberCard: String) {
        viewModelScope.launch {
            try {
                _Home_uiState.value = HomeUIState.Loading
                binNetRepository.getCardBIN(numberCard)
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        Log.d("ERROR_API", e.message.toString())
                        _Home_uiState.value = HomeUIState.Error("Ошибка получения данных, проверьте интернет соединение")
                    }
                    .collect {
                        val bin = numberCard.replace(" ", "").toInt()
                        Log.d("RESPONSE_API", it.toCardInfo(bin).toString())
                        _Home_uiState.value = HomeUIState.SuccessCard(it.toCardInfo(bin))
                        if (it.brand == null) {
                            Log.d("DATA_SEARCH", "Данные пустые")
                            _Home_uiState.value = HomeUIState.Error("Такой карты нет")
                        } else {
                            Log.d("ADD_DATA_SEARCH", "Дошло до добавления")
                            addCard(it.toCardInfo(bin))
                            Log.d("ADD_DATA_SEARCH", "Добавило")
                        }
                    }
            } catch (e: IOException) {
                _Home_uiState.value = HomeUIState.Error("Ошибка получения данных, проверьте интернет соединение")
            }
        }
    }

    private fun getAllCards() {
        viewModelScope.launch {
            try {
                binDBRepository.getAllCards()
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        Log.d("DB_ERROR", e.message.toString())
                        getAllCards()
                    }
                    .collect {
                        _cards.value = it
                    }
            } catch (e: IOException) {
                _Home_uiState.value = HomeUIState.Error("Ошибка получения карт")
            }
        }
    }

    private fun addCard(card: CardInfo) {
        viewModelScope.launch {
            try {
                binDBRepository.insertCard(card)
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        Log.d("DB_ERROR", e.message.toString())
                        _Home_uiState.value = HomeUIState.Error("Ошибка добавления карты")
                    }
                    .collect {
                        _Home_uiState.value = HomeUIState.SuccessCard(card)
                        getAllCards()
                    }
            } catch (e: IOException) {
                _Home_uiState.value = HomeUIState.Error("Ошибка добавления карты")
            }

        }
    }

    fun searchCard(numberCard: String) {
        isSendRequest = true
        if (_cards.value != emptyList<CardInfo>()) {
            val card = numberCard.replace(" ", "")
            var isExist = false
            Log.d("DATA_SEARCH", "Ищет совпадения в БД")
            for (curCard in _cards.value) {
                val strCard = curCard.id.toString()
                if (strCard.length <= card.length) {
                    if (numberEquals(card, strCard)) {
                        _Home_uiState.value = HomeUIState.SuccessCard(curCard)
                        Log.d("DATA_SEARCH", "Нашло совпадения")
                        isExist = true
                        break
                    }
                } else {
                    if (numberEquals(strCard, card)) {
                        _Home_uiState.value = HomeUIState.SuccessCard(curCard)
                        Log.d("DATA_SEARCH", "Нашло совпадения")
                        isExist = true
                        break
                    }
                }
            }

            if (!isExist) {
                Log.d("DATA_SEARCH", "Не нашло совпадения")
                getCardFromApi(numberCard)
            }
            isSendRequest = false
            return
        }
    }

    private fun numberEquals(number: String, curCard: String): Boolean {
        var countEquals = 0
        for (i in curCard.indices) {
            if (number[i] == curCard[i]) {
                countEquals++
            }
        }
        if (countEquals == curCard.length) {
            Log.d("DATA_SEARCH","Карта найдена: $curCard")
            return true
        }
        return false
    }
}