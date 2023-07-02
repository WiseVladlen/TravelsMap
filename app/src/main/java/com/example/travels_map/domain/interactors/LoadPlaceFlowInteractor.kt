package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IPlaceRepository
import javax.inject.Inject

class LoadPlaceFlowInteractor @Inject constructor(private val placeRepository: IPlaceRepository) {

    fun run() = placeRepository.placeFlow
}