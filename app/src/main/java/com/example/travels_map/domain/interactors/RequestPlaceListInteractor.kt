package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IPlaceRepository
import javax.inject.Inject

class RequestPlaceListInteractor @Inject constructor(private val placeRepository: IPlaceRepository) {

    suspend fun run() = placeRepository.requestPlaceList()
}