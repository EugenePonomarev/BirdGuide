package com.greencodemoscow.redbook.map.presentation.model

import com.greencodemoscow.redbook.core.data.request.RedBookRequestItem
import com.greencodemoscow.redbook.map.presentation.viewmodel.Points

data class MapFragmentState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val redBookItems: List<RedBookRequestItem>? = null,
    val animalList: List<Points> = emptyList(),
    val plantList: List<Points> = emptyList()
)