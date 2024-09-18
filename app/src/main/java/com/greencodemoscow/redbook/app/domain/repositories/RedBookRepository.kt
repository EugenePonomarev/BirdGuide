package com.greencodemoscow.redbook.app.domain.repositories

import com.greencodemoscow.redbook.core.data.request.RedBookRequest
import com.greencodemoscow.redbook.core.data.request.RedBookRequestItem
import com.greencodemoscow.redbook.map.data.model.PointData
import kotlinx.coroutines.flow.Flow

interface RedBookRepository {
    suspend fun getRedBookInfo(): Flow<List<RedBookRequestItem>>

    fun getRedBookItems(): Flow<List<RedBookRequestItem>>
}