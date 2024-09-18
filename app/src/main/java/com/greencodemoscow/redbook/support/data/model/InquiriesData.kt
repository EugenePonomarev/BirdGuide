package com.greencodemoscow.redbook.support.data.model

data class InquiriesData(
    val name: String,
    val descritption: String,
    val status: String,
    val date: String,
    val location: String,
    val photo: Int? = null
)
