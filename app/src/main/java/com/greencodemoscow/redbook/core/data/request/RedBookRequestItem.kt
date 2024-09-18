package com.greencodemoscow.redbook.core.data.request

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.UUID
import kotlin.random.Random

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

// Генерация случайных координат для Москворецкого парка
fun getRandomLocation(): Location {
    val latitude = 55.707 + Random.nextDouble(0.001, 0.01)
    val longitude = 37.501 + Random.nextDouble(0.001, 0.01)
    return Location(
        id = UUID.randomUUID().toString(),
        latitude = latitude.toString(),
        longitude = longitude.toString()
    )
}

// Список редких животных
val animals = listOf(
    "Амурский тигр", "Снежный барс", "Дальневосточный леопард",
    "Белый медведь", "Зубр", "Европейская норка",
    "Кулан", "Чёрный аист", "Медведь гималайский", "Орел-беркут"
)

// Список редких растений
val plants = listOf(
    "Сибирская сосна", "Кавказский подснежник", "Кувшинка белая",
    "Венерин башмачок", "Рябчик шахматный", "Лилия кудреватая",
    "Калужница болотная", "Очиток едкий", "Багульник болотный", "Ковыль перистый"
)

// Генерация данных для животных
fun generateAnimalData(): List<RedBookRequestItem> {
    return animals.mapIndexed { index, animal ->
        RedBookRequestItem(
            category = Category(id = UUID.randomUUID().toString(), name = "Animal"),
            count = Random.nextInt(1, 50),
            description = "Редкое животное: $animal",
            id = UUID.randomUUID().toString(),
            imageUrl = "https://example.com/animal$index.jpg",
            location = getRandomLocation(),
            name = animal
        )
    }
}

// Генерация данных для растений
fun generatePlantData(): List<RedBookRequestItem> {
    return plants.mapIndexed { index, plant ->
        RedBookRequestItem(
            category = Category(id = UUID.randomUUID().toString(), name = "Plant"),
            count = Random.nextInt(1, 50),
            description = "Редкое растение: $plant",
            id = UUID.randomUUID().toString(),
            imageUrl = "https://example.com/plant$index.jpg",
            location = getRandomLocation(),
            name = plant
        )
    }
}

// Создание финального списка всех данных
fun generateTestData(): List<RedBookRequestItem> {
    val animalData = generateAnimalData()
    val plantData = generatePlantData()
    return animalData + plantData
}