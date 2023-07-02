package com.example.travels_map.domain.entities

import com.yandex.mapkit.geometry.Point

data class Route(
    val id: String,
    val name: String,
    val wayPoints: List<Point>,
    val requestPoints: List<Point>,
) {
    companion object {
        const val CLASS_NAME = "Route"

        const val KEY_NAME = "name"
        const val KEY_WAY_POINTS = "wayPoints"
        const val KEY_REQUEST_POINTS = "requestPoints"
    }
}