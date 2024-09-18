package com.greencodemoscow.redbook.core.data.repository

import android.util.Log
import com.greencodemoscow.redbook.core.data.request.RedBookRequest
import com.greencodemoscow.redbook.redBook.data.api.RedBookApi
import com.greencodemoscow.redbook.app.domain.repositories.RedBookRepository
import com.greencodemoscow.redbook.core.data.db.RedBookDao
import com.greencodemoscow.redbook.core.data.request.RedBookRequestItem
import com.greencodemoscow.redbook.core.data.request.generateTestData
import com.greencodemoscow.redbook.map.data.model.PointData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RedBookRepositoryImpl @Inject constructor(
    private val api: RedBookApi,
    private val redBookDao: RedBookDao
) : RedBookRepository {

    override suspend fun getRedBookInfo(): Flow<List<RedBookRequestItem>> = flow {
        try {
            // Make the API call using Retrofit suspend function
            val response = api.getRedBookInfo()

            // Handle the response directly (response is the body)
            if (response.isNotEmpty()) {
                // Insert into the database if response is not empty
                redBookDao.insertAll(response)
            } else {
                redBookDao.insertAll(generateTestData())
            }

            // Emit the flow from the database
            emitAll(redBookDao.getRedBookItems())

        } catch (e: Exception) {
            // Handle the exception and log the error
            Log.e("API_ERROR", "Error fetching data: ${e.message}")
            emitAll(redBookDao.getRedBookItems()) // Still emit database data
        }
    }.flowOn(Dispatchers.IO)  // Use IO thread for database and network operations

    override fun getRedBookItems(): Flow<List<RedBookRequestItem>> {
        return redBookDao.getRedBookItems()
    }
}

