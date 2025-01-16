package com.example.binchecker.repository

import com.example.binchecker.data.network.ApiService
import com.example.binchecker.data.network.dto.BINCardDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BINNetworkRepositoryImpl @Inject constructor(private val apiService: ApiService): BINNetworkRepository {

    override fun getCardBIN(cardBIN: String): Flow<BINCardDto> = flow {
        emit(apiService.getCardBIN(cardBIN))
    }

}