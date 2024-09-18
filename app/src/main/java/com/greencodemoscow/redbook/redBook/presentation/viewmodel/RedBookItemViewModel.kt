package com.greencodemoscow.redbook.redBook.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greencodemoscow.redbook.app.domain.LocationTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedBookItemViewModel @Inject constructor(
    private val locationTracker: LocationTracker
) : ViewModel() {

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