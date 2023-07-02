package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IDrivingRouteRepository
import com.yandex.mapkit.geometry.Point
import javax.inject.Inject

class RequestDrivingRouteInteractor @Inject constructor(private val drivingRouteRepository: IDrivingRouteRepository) {

    suspend fun run(points: List<Point>) = drivingRouteRepository.requestDrivingRoute(points)
}