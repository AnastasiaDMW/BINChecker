package com.example.binchecker.data.network.dto

import com.example.binchecker.data.database.entity.BankCard
import com.example.binchecker.data.database.entity.CardInfo
import com.example.binchecker.data.database.entity.CountryCard
import com.example.binchecker.data.database.entity.NumberCard

data class BINCardDto(
    val scheme: String,
    val type: String,
    val brand: String,
    val prepaid: Boolean,
    val number: NumberDto,
    val country: CountryDto,
    val bank: BankDto
)

fun BINCardDto.toCardInfo(id: Int): CardInfo =
    CardInfo(
        id = id,
        scheme = this.scheme,
        type = this.type,
        brand = this.brand,
        prepaid = this.prepaid,
        number = NumberCard(
            length = this.number.length,
            luhn = this.number.luhn
        ),
        country = CountryCard(
            numeric = this.country.numeric,
            alpha2 = this.country.alpha2,
            name = this.country.name,
            emoji = this.country.emoji,
            currency = this.country.currency,
            latitude = this.country.latitude,
            longitude = this.country.longitude
        ),
        bank = BankCard(
            name = this.bank.name,
            url = this.bank.url,
            phone = this.bank.phone,
            city = this.bank.city,
        )
    )