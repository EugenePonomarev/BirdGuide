package com.greencodemoscow.redbook.core.data.db

import androidx.room.TypeConverter
import com.greencodemoscow.redbook.core.data.request.Location

class LocationConverter {

    @TypeConverter
    fun fromLocation(location: Location): String {
        // Convert the Location object to a string format to store in the database
        return "${location.id}|${location.latitude}|${location.longitude}"
    }

    @TypeConverter
    fun toLocation(locationString: String): Location {
        // Convert the string back into the Location object
        val parts = locationString.split("|")
        return Location(parts[0], parts[1], parts[2])
    }
}