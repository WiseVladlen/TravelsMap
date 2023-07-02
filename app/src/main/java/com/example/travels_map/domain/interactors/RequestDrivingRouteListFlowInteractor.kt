package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IDrivingRouteRepository
import javax.inject.Inject

class RequestDrivingRouteListFlowInteractor @Inject constructor(private val drivingRouteRepository: IDrivingRouteRepository) {

    suspend fun run() = drivingRouteRepository.requestDrivingRouteList()
}