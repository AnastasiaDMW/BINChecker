package com.example.binchecker.repository

import com.example.binchecker.data.network.dto.BINCardDto
import kotlinx.coroutines.flow.Flow

interface BINNetworkRepository {

    fun getCardBIN(cardBIN: String): Flow<BINCardDto>

}