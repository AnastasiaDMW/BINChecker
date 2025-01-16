package com.example.binchecker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "bank")
data class BankCard(
    @ColumnInfo("bank_name")
    val name: String,
    val url: String,
    val phone: String,
    val city: String
)