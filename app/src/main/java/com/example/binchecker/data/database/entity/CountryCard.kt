package com.example.binchecker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "country")
data class CountryCard(
    val numeric: String,
    val alpha2: String,
    @ColumnInfo("country_name")
    val name: String,
    val emoji: String,
    val currency: String,
    val latitude: Int,
    val longitude: Int
)