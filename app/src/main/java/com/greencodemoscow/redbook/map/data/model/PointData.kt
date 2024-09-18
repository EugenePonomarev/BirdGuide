package com.greencodemoscow.redbook.map.data.model
import com.yandex.mapkit.geometry.Point

data class PointData(
    val name: String,
    val description: String,
    val coordinatePoints: List<Point>,
    val photo: String
)