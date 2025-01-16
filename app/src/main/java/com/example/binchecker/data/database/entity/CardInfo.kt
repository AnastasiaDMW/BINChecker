package com.example.binchecker.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_info")
data class CardInfo(
    @PrimaryKey
    val id: Int,
    val scheme: String,
    val type: String,
    val brand: String,
    val prepaid: Boolean,
    @Embedded
    val number: NumberCard,
    @Embedded
    val country: CountryCard,
    @Embedded
    val bank: BankCard
)