package com.example.binchecker.data.network.dto

data class BINCardDto(
    val scheme: String,
    val type: String,
    val brand: String,
    val prepaid: Boolean,
    val number: NumberDto,
    val country: CountryDto,
    val bank: BankDto
)
