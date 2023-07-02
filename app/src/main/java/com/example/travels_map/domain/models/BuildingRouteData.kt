package com.example.travels_map.domain.models

import com.yandex.mapkit.geometry.Point

data class BuildingRouteData(
    val name: String,
    val wayPoints: List<Point>,
    val requestPoints: List<Point>
)