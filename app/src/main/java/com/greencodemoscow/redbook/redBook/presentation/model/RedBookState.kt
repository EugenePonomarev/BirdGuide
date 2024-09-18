package com.greencodemoscow.redbook.redBook.presentation.model

import com.greencodemoscow.redbook.redBook.data.model.RedBookItem

data class RedBookState(
    val searchText: String = "",
    val selectedPark: String? = null,
    val isAnimalsChecked: Boolean = true,
    val isPlantsChecked: Boolean = true,
    val items: List<RedBookItem> = emptyList(),
    val searchList: List<RedBookItem> = emptyList()
)