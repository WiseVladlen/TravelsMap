package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IDrivingRouteRepository
import javax.inject.Inject

class CancelDrivingSessionInteractor @Inject constructor(private val drivingRouteRepository: IDrivingRouteRepository) {

    fun run() = drivingRouteRepository.cancelDrivingSession()
}