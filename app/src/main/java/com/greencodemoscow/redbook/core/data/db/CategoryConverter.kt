package com.greencodemoscow.redbook.core.data.db

import androidx.room.TypeConverter
import com.greencodemoscow.redbook.core.data.request.Category

class CategoryConverter {

    @TypeConverter
    fun fromCategory(category: Category): String {
        // Convert the Category object to a single String to store in the database
        return "${category.id}|${category.name}"
    }

    @TypeConverter
    fun toCategory(categoryString: String): Category {
        // Split the string back into the Category object
        val parts = categoryString.split("|")
        return Category(parts[0], parts[1])
    }
}
