package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IDrivingRouteRepository
import javax.inject.Inject

class CreateRouteInteractor @Inject constructor(private val drivingRouteRepository: IDrivingRouteRepository) {

    suspend fun run(name: String): Result<Nothing?> = drivingRouteRepository.create(name)
}