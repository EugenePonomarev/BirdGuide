package com.greencodemoscow.redbook.map.presentation.model

import com.greencodemoscow.redbook.map.data.model.PointData

sealed class MapAction {
    data object LoadMapData : MapAction()
    data object LocationTracker : MapAction()
    data class OnMapObjectClicked(val point: PointData) : MapAction()
}