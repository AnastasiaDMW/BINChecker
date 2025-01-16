package com.example.binchecker.repository

import com.example.binchecker.data.database.entity.CardInfo
import kotlinx.coroutines.flow.Flow

interface BINDatabaseRepository {

    fun getAllCards(): Flow<List<CardInfo>>

    fun insertCard(card: CardInfo): Flow<Unit>

}