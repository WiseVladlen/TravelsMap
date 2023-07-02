package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.repositories.IPlaceRepository
import javax.inject.Inject

class SavePlaceInteractor @Inject constructor(private val placeRepository: IPlaceRepository) {

    suspend fun run(place: Place) = placeRepository.save(place)
}