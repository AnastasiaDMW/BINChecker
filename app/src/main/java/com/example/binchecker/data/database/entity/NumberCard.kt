package com.example.binchecker.data.database.entity

import androidx.room.Entity

@Entity(tableName = "number")
data class NumberCard(
    val length: Int,
    val luhn: Boolean
)