package com.greencodemoscow.redbook.redBook.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RedBookTypes(
    val animals: List<RedBookItem> = emptyList(),
    val plants: List<RedBookItem> = emptyList(),
    val other: List<RedBookItem> = emptyList()
): Parcelable