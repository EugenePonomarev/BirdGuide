package com.greencodemoscow.redbook.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greencodemoscow.redbook.app.domain.LocationTracker
import com.greencodemoscow.redbook.app.domain.repositories.RedBookRepository
import com.greencodemoscow.redbook.app.presentation.model.MainActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationTracker: LocationTracker,
    private val repository: RedBookRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getRedBookInfo()
                .onStart {
                    _state.value = _state.value.copy(isLoading = true)
                    delay(5000)
                }
                .onCompletion { }
                .catch {
                    _state.value = _state.value.copy(isError = true, isLoading = false)
                }
                .collect {
                    _state.value = _state.value.copy(isError = false, isLoading = false, data = it)
                    Log.e("TAG_DATA", "result: $it")
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