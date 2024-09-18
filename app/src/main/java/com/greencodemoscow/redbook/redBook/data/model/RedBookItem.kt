package com.greencodemoscow.redbook.redBook.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RedBookItem(
    val description: String,
    val image: String,
    val name: String,
    val type: String? = null,
    val habitatArea: String? = null,
    val latitude: String? = null,
    val longitude: String? = null
) : Parcelable