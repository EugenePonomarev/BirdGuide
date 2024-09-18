package com.greencodemoscow.redbook.redBook.data.api

import com.greencodemoscow.redbook.core.data.request.RedBookRequest
import com.greencodemoscow.redbook.core.data.request.RedBookRequestItem
import retrofit2.Call
import retrofit2.http.GET

interface RedBookApi {
    @GET("rest/red_book_info/")
    suspend fun getRedBookInfo(): List<RedBookRequestItem>
}