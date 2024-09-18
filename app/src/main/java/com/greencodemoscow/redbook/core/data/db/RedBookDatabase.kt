package com.greencodemoscow.redbook.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.greencodemoscow.redbook.core.data.request.RedBookRequestItem

@Database(entities = [RedBookRequestItem::class], version = 1)
@TypeConverters(CategoryConverter::class, LocationConverter::class)
abstract class RedBookDatabase : RoomDatabase() {
    abstract fun redBookDao(): RedBookDao
}