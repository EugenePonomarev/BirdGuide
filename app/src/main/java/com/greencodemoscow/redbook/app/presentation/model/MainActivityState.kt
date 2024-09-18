package com.greencodemoscow.redbook.app.presentation.model

import com.greencodemoscow.redbook.core.data.request.RedBookRequestItem

data class MainActivityState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val data: List<RedBookRequestItem>? = null
)