package com.greencodemoscow.redbook.redBook.presentation.model

import android.content.Context

sealed class RedBookAction {
    data class Search(val query: String) : RedBookAction()
    data class FilterByType(val isAnimalsChecked: Boolean, val isPlantsChecked: Boolean) : RedBookAction()
    data class FilterByPark(val selectedPark: String) : RedBookAction()
    data object ShowAllItems : RedBookAction()
    data class Initialize(val context: Context) : RedBookAction()
}