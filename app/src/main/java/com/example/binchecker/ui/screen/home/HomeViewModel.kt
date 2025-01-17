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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val binNetRepository: BINNetworkRepository,
    private val binDBRepository: BINDatabaseRepository
): ViewModel() {

    private var isSendRequest = false
    var isRequestDataFromApiError = false

    private val _searchInput = MutableStateFlow("")

    private val _homeUiState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState.SuccessCard(null))
    val homeUiState: StateFlow<HomeUIState> = _homeUiState

    private val _cards: MutableStateFlow<List<CardInfo>> = MutableStateFlow(emptyList())
    private val cards: StateFlow<List<CardInfo>> = _cards

    private var currentJob: Job? = null

    init {
        getAllCards()
        viewModelScope.launch {
            _searchInput
                .debounce(300)
                .filter { input -> input.length in 6..8 && !isSendRequest }
                .collect { input ->
                    if (currentJob?.isActive == true) {
                        currentJob?.cancel()
                    }
                    searchCard(input)
                }
        }
    }

    private fun getCardFromApi(numberCard: String) {
        currentJob = viewModelScope.launch {
            try {
                _homeUiState.value = HomeUIState.Loading
                binNetRepository.getCardBIN(numberCard)
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        Log.d("ERROR_API", e.message.toString())
                        isRequestDataFromApiError = true
                        _homeUiState.value = HomeUIState.Error("Попробуйте позже")
                        isSendRequest = false
                        return@catch
                    }
                    .collect {
                        val bin = numberCard.replace(" ", "").toInt()
                        Log.d("RESPONSE_API", it.toCardInfo(bin).toString())
                        _homeUiState.value = HomeUIState.SuccessCard(it.toCardInfo(bin))
                        Log.d("ADD_DATA_SEARCH", "Дошло до добавления")
                        addCard(it.toCardInfo(bin))
                        Log.d("ADD_DATA_SEARCH", "Добавило")
                        isSendRequest = false
                    }

            } catch (e: IOException) {
                _homeUiState.value = HomeUIState.Error("Ошибка получения данных, проверьте интернет соединение")
                isSendRequest = false
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
                        Log.d("ALL_CARDS", "Cards: ${cards.value}")
                    }
            } catch (e: IOException) {
                _homeUiState.value = HomeUIState.Error("Ошибка получения карт")
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
                        _homeUiState.value = HomeUIState.Error("Ошибка добавления карты")
                    }
                    .collect {
                        _homeUiState.value = HomeUIState.SuccessCard(card)
                        getAllCards()
                    }
            } catch (e: IOException) {
                _homeUiState.value = HomeUIState.Error("Ошибка добавления карты")
            }

        }
    }

    fun onInputChange(numberCard: String) {
        _searchInput.value = numberCard
    }

    private fun searchCard(numberCard: String) {
        if (!isSendRequest) {
            isSendRequest = true
            Log.d("ALL_CARDS", "cards: $cards")
            if (_cards.value != emptyList<CardInfo>()) {
                val card = numberCard.replace(" ", "")
                var isExist = false
                Log.d("DATA_SEARCH", "Ищет совпадения в БД")
                for (curCard in _cards.value) {
                    val strCard = curCard.id.toString()
                    if (strCard.length <= card.length) {
                        if (numberEquals(card, strCard)) {
                            _homeUiState.value = HomeUIState.SuccessCard(curCard)
                            Log.d("DATA_SEARCH", "Нашло совпадения")
                            isExist = true
                            break
                        }
                    } else {
                        if (numberEquals(strCard, card)) {
                            _homeUiState.value = HomeUIState.SuccessCard(curCard)
                            Log.d("DATA_SEARCH", "Нашло совпадения")
                            isExist = true
                            break
                        }
                    }
                }

                if (!isExist) {
                    Log.d("DATA_SEARCH", "Не нашло совпадения")
                    getCardFromApi(numberCard)
                } else {
                    isSendRequest = false
                }
            } else {
                getCardFromApi(numberCard)
            }
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