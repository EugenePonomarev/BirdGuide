package com.greencodemoscow.redbook.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.greencodemoscow.redbook.core.data.request.RedBookRequestItem
import kotlinx.coroutines.flow.Flow

@Dao
interface RedBookDao {
    @Query("SELECT * FROM red_book_items")
    fun getRedBookItems(): Flow<List<RedBookRequestItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RedBookRequestItem>)

    @Query("DELETE FROM red_book_items")
    suspend fun deleteAll()
}