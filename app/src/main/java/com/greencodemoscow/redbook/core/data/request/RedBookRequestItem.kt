package com.greencodemoscow.redbook.core.data.request

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "red_book_items")
data class RedBookRequestItem(
    val category: Category,
    val count: Int,
    val description: String,
    @PrimaryKey val id: String,
    @field:Json(name = "image_url")
    val imageUrl: String,
    val location: Location? = null,
    val name: String
)