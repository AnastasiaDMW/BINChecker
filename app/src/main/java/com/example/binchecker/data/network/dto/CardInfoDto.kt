package com.example.binchecker.data.network.dto

import com.example.binchecker.data.database.entity.CardInfo

data class CardInfoDto(
    val scheme: String,
    val type: String,
    val brand: String,
    val prepaid: Boolean
)

fun CardInfo.toCardInfoDto() = CardInfoDto(
    scheme = this.scheme,
    type = this.type,
    brand = this.brand,
    prepaid = this.prepaid
)

fun CardInfo.toNumberDto() = NumberDto(
    length = this.number.length,
    luhn = this.number.luhn
)

fun CardInfo.toCountryDto() = CountryDto(
    numeric = this.country.numeric,
    alpha2 = this.country.alpha2,
    name = this.country.name,
    emoji = this.country.emoji,
    currency = this.country.currency,
    latitude = this.country.latitude,
    longitude = this.country.longitude
)

fun CardInfo.toBankDto() = BankDto(
    name = this.bank.name,
    url = this.bank.url,
    phone = this.bank.phone,
    city = this.bank.city
)