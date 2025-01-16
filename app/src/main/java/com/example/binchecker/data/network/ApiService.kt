package com.example.binchecker.data.network

import com.example.binchecker.data.network.dto.BINCardDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{cardBIN}")
    suspend fun getCardBIN(@Path("cardBIN") cardBIN: String): BINCardDto

}