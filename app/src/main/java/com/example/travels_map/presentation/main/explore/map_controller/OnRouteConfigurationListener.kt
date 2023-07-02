package com.example.travels_map.presentation.main.explore.map_controller

import com.yandex.mapkit.geometry.Point

interface OnRouteConfigurationListener {
    fun addPoint(point: Point)
    fun removePoint(point: Point)
}