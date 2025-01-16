package com.example.binchecker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.binchecker.data.database.dao.BINCardDao
import com.example.binchecker.data.database.entity.CardInfo

@Database(entities = [CardInfo::class], version = 1, exportSchema = false)
abstract class BINCardDatabase: RoomDatabase() {

    abstract fun binCardDao(): BINCardDao

}