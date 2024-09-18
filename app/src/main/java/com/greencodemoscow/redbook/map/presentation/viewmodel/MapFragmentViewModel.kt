package com.greencodemoscow.redbook.map.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greencodemoscow.redbook.app.domain.LocationTracker
import com.greencodemoscow.redbook.app.domain.repositories.RedBookRepository
import com.greencodemoscow.redbook.core.data.request.RedBookRequestItem
import com.greencodemoscow.redbook.map.presentation.model.MapFragmentState
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ANIMAL_TAG = "Animal"
private const val PLANT_TAG = "Plant"

data class Points(
    val item: RedBookRequestItem,
    val points: Point
)

@HiltViewModel
class MapFragmentViewModel @Inject constructor(
    private val locationTracker: LocationTracker,
    private val repository: RedBookRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MapFragmentState())
    val state = _state.asStateFlow()

    // Expose the RedBook items from the repository
    private val _redBookItems = repository.getRedBookItems()
    val redBookItems: Flow<List<RedBookRequestItem>> = _redBookItems

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            redBookItems
                .onStart {
                    _state.value = _state.value.copy(isError = false, isLoading = true)
                }
                .onCompletion { }
                .catch {
                    _state.value = _state.value.copy(isError = true, isLoading = false)
                }
                .collect { items ->
                    // Filter lists
                    // Filter and map items into two lists of Points
                    val animalPoints = items.filter {
                        it.category.name == ANIMAL_TAG && it.location != null
                    }.mapNotNull { item ->
                        item.location?.let { location ->
                            try {
                                Points(
                                    item = item,
                                    points = Point(location.latitude.toDouble(), location.longitude.toDouble())
                                )
                            } catch (e: NumberFormatException) {
                                null // skip invalid locations
                            }
                        }
                    }

                    val plantPoints = items.filter {
                        it.category.name == PLANT_TAG && it.location != null
                    }.mapNotNull { item ->
                        item.location?.let { location ->
                            try {
                                Points(
                                    item = item,
                                    points = Point(location.latitude.toDouble(), location.longitude.toDouble())
                                )
                            } catch (e: NumberFormatException) {
                                null // skip invalid locations
                            }
                        }
                    }

                    // Update state with filtered lists
                    _state.value = _state.value.copy(
                        isError = false,
                        isLoading = false,
                        redBookItems = items,
                        animalList = animalPoints,
                        plantList = plantPoints
                    )
                }
        }
    }

    fun locationTracker() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                Log.e(
                    "TAG_LOCATION",
                    "latitude: ${location.latitude} longitude: ${location.longitude}"
                )
            } ?: kotlin.run {
                Log.e(
                    "TAG_LOCATION",
                    "Error: Couldn't retrieve location. Make sure to grand permission and enable GPS"
                )
            }
        }
    }
}