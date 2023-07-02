package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IDrivingRouteRepository
import javax.inject.Inject

class LoadDrivingRouteFlowInteractor @Inject constructor(private val drivingRouteRepository: IDrivingRouteRepository) {

    fun run() = drivingRouteRepository.buildingDrivingRouteFlow
}