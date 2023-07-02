package com.example.travels_map.domain.repositories

import com.example.travels_map.domain.entities.Route
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.Error
import kotlinx.coroutines.flow.SharedFlow

interface IDrivingRouteRepository : DrivingSession.DrivingRouteListener {
    val drivingRouteListFlow: SharedFlow<Result<List<Route>>>
    val buildingDrivingRouteFlow: SharedFlow<Result<DrivingRoute?>>

    suspend fun requestDrivingRouteList()
    suspend fun create(name: String): Result<Nothing?>

    override fun onDrivingRoutes(routeList: MutableList<DrivingRoute>)
    override fun onDrivingRoutesError(error: Error)

    suspend fun requestDrivingRoute(points: List<Point>)

    fun cancelDrivingSession()
}