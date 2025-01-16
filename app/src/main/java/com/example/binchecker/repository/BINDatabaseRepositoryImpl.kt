package com.example.binchecker.repository

import com.example.binchecker.data.database.dao.BINCardDao
import com.example.binchecker.data.database.entity.CardInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BINDatabaseRepositoryImpl @Inject constructor(private val binDao: BINCardDao): BINDatabaseRepository {

    override fun getAllCards(): Flow<List<CardInfo>> = flow {
        emit(binDao.getAllCards())
    }

    override fun insertCard(card: CardInfo): Flow<Unit> = flow {
        binDao.insertCard(card)
        emit(Unit)
    }

}