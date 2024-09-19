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


val locations = mutableListOf(
    Location(id = UUID.randomUUID().toString(), "55.815018", "37.436728"),
    Location(id = UUID.randomUUID().toString(), "55.809023", "37.420462"),
    Location(id = UUID.randomUUID().toString(), "55.812611", "37.413236"),
    Location(id = UUID.randomUUID().toString(), "55.807283", "37.427879"),
    Location(id = UUID.randomUUID().toString(), "55.806574", "37.439846"),
    Location(id = UUID.randomUUID().toString(), "55.804167", "37.442217"),
    Location(id = UUID.randomUUID().toString(), "55.803157", "37.419200"),
    Location(id = UUID.randomUUID().toString(), "55.812138", "37.413962"),
    Location(id = UUID.randomUUID().toString(), "55.813148", "37.413427"),
    Location(id = UUID.randomUUID().toString(), "55.816772", "37.406996"),
    Location(id = UUID.randomUUID().toString(), "55.822154", "37.406095"),
    Location(id = UUID.randomUUID().toString(), "55.815634", "37.420113"),
    Location(id = UUID.randomUUID().toString(), "55.795867", "37.416559"),
    Location(id = UUID.randomUUID().toString(), "55.791352", "37.428214"),
    Location(id = UUID.randomUUID().toString(), "55.789077", "37.424963"),
    Location(id = UUID.randomUUID().toString(), "55.793913", "37.438959"),
    Location(id = UUID.randomUUID().toString(), "55.803778", "37.442232"),
    Location(id = UUID.randomUUID().toString(), "55.803440", "37.430341"),
    Location(id = UUID.randomUUID().toString(), "55.810313", "37.437047"),
    Location(id = UUID.randomUUID().toString(), "55.813047", "37.444549")
)

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

fun getUniqueLocation(): Location {
    if (locations.isNotEmpty()) {
        return locations.removeAt(0) // Возвращаем и удаляем локацию из списка
    } else {
        throw IllegalStateException("Локации закончились") // Обрабатываем случай нехватки локаций
    }
}

// Генерация данных для животных с использованием уникальных локаций
fun generateAnimalData(): List<RedBookRequestItem> {
    return animals.mapIndexed { index, animal ->
        RedBookRequestItem(
            category = Category(id = UUID.randomUUID().toString(), name = "Animal"),
            count = Random.nextInt(1, 50),
            description = "Редкое животное: $animal",
            id = UUID.randomUUID().toString(),
            imageUrl = "https://example.com/animal$index.jpg",
            location = getUniqueLocation(),
            name = animal
        )
    }
}

// Генерация данных для растений с использованием уникальных локаций
fun generatePlantData(): List<RedBookRequestItem> {
    return plants.mapIndexed { index, plant ->
        RedBookRequestItem(
            category = Category(id = UUID.randomUUID().toString(), name = "Plant"),
            count = Random.nextInt(1, 50),
            description = "Редкое растение: $plant",
            id = UUID.randomUUID().toString(),
            imageUrl = "https://example.com/plant$index.jpg",
            location = getUniqueLocation(),
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