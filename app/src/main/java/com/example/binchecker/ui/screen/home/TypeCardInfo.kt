package com.example.binchecker.ui.screen.home

sealed class TypeCardInfo {
    data object Card: TypeCardInfo()
    data object Country: TypeCardInfo()
    data object Bank: TypeCardInfo()
}