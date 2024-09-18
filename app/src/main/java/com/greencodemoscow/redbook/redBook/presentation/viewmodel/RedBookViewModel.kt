package com.greencodemoscow.redbook.redBook.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.greencodemoscow.redbook.redBook.data.model.RedBookItem
import com.greencodemoscow.redbook.redBook.data.model.RedBookTypes
import com.greencodemoscow.redbook.redBook.presentation.model.RedBookAction
import com.greencodemoscow.redbook.redBook.presentation.model.RedBookState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

private const val ANIMAL_TAG = "Animal"
private const val PLANT_TAG = "Plant"
private const val OTHER_TAG = "Plant"

@HiltViewModel
class RedBookViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(RedBookState())
    val state: StateFlow<RedBookState> = _state.asStateFlow()

    init {
        initializeItems()
    }

    fun sendAction(action: RedBookAction) {
        when (action) {
            is RedBookAction.Search -> updateSearchResults(action.query)
            is RedBookAction.FilterByType -> filterByType(action.isAnimalsChecked, action.isPlantsChecked)
            is RedBookAction.FilterByPark -> filterByPark(action.selectedPark)
            is RedBookAction.ShowAllItems -> showAllItems()
            is RedBookAction.Initialize -> initializeItems()
        }
    }

    private fun updateSearchResults(query: String) {
        val normalizedQuery = normalizeSearchQuery(query)
        val filteredList = _state.value.items.filter {
            it.name.contains(Regex(normalizedQuery, RegexOption.IGNORE_CASE)) &&
                    ((_state.value.isAnimalsChecked && it.type == ANIMAL_TAG) ||
                            (_state.value.isPlantsChecked && it.type == PLANT_TAG)) &&
                    (_state.value.selectedPark == null || _state.value.selectedPark == it.habitatArea)
        }

        val finalList = if (_state.value.isAnimalsChecked && _state.value.isPlantsChecked) {
            _state.value.items.filter {
                it.name.contains(Regex(normalizedQuery, RegexOption.IGNORE_CASE)) &&
                        (_state.value.selectedPark == null || _state.value.selectedPark == it.habitatArea)
            }
        } else {
            filteredList
        }

        _state.value = _state.value.copy(searchList = finalList)
    }

    private fun normalizeSearchQuery(query: String): String {
        return query.replace("е", "[её]")
    }

    private fun filterByType(isAnimalsChecked: Boolean, isPlantsChecked: Boolean) {
        val filteredList = _state.value.items.filter {
            (isAnimalsChecked && it.type == ANIMAL_TAG) ||
                    (isPlantsChecked && it.type == PLANT_TAG)
        }

        val finalList = if (isAnimalsChecked && isPlantsChecked) {
            _state.value.items
        } else {
            filteredList
        }

        _state.value = _state.value.copy(
            isAnimalsChecked = isAnimalsChecked,
            isPlantsChecked = isPlantsChecked,
            searchList = finalList
        )
    }

    private fun initializeItems() {
        val items = generateTestData()
        _state.value = _state.value.copy(items = items, searchList = items)
    }

    private fun filterByPark(selectedPark: String) {
        val filteredList = _state.value.items.filter { item ->
            (_state.value.isAnimalsChecked && item.type == ANIMAL_TAG ||
                    _state.value.isPlantsChecked && item.type == PLANT_TAG) &&
                    (selectedPark.isEmpty() || item.habitatArea == selectedPark)
        }

        _state.value = _state.value.copy(selectedPark = selectedPark, searchList = filteredList)
    }

    private fun showAllItems() {
        _state.value = _state.value.copy(searchList = _state.value.items)
    }

    private val parks = listOf(
        "Александровский сад" to "55.7517, 37.6176 E",
        "Измайловский парк" to "55.7907 N, 37.7483 E",
        "Коломенское" to "55.6738 N, 37.6647 E",
        "Лосиный Остров" to "55.8350 N, 37.7600 E",
        "Сокольники" to "55.7949 N, 37.6745 E",
        "Парк Горького" to "55.7296 N, 37.6030 E",
        "Царицыно" to "55.6129 N, 37.6695 E",
        "Ботанический сад МГУ" to "55.7012 N, 37.5300 E",
        "Воробьёвы горы" to "55.7100 N, 37.5550 E",
        "Нескучный сад" to "55.7248 N, 37.5891 E"
    )

    fun getParksList(): List<String> {
        return parks.map { it.first }
    }

    private fun generateTestData(): List<RedBookItem> {
        val plants = listOf(
            RedBookItem(name = "Ландыш майский", image = "images/Ландыш_майский.jfif", description =  "Многолетнее травянистое растение с красивыми белыми ароматными цветами...", type =  PLANT_TAG),
            RedBookItem(name ="Венерин башмачок", image = "images/Венерин_башмачок.jfif", description = "Редкий вид орхидеи, который встречается в тенистых и влажных лесах...", type = PLANT_TAG),
            // Add other plant items here...
        )

        val animals = listOf(
            RedBookItem(name ="Лесная мышовка", image ="images/Лесная_мышовка.jpg", description = "Маленький грызун длиной до 7,6 см с длинным хвостом...", type = ANIMAL_TAG),
            RedBookItem(name ="Орешниковая соня", image ="images/Орешниковая_соня.jfif", description = "Небольшой грызун длиной до 15 см с пушистым хвостом...",type =  ANIMAL_TAG),
            // Add other animal items here...
        )

        return plants + animals
    }
}
