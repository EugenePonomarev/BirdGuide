package com.greencodemoscow.redbook.app.domain

import android.location.Location

interface LocationTracker {

    suspend fun getCurrentLocation(): Location?
}