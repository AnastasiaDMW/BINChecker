package com.example.binchecker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.binchecker.data.database.entity.CardInfo

@Dao
interface BINCardDao {

    @Query("SELECT * FROM card_info")
    fun getAllCards(): List<CardInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(card: CardInfo)

}