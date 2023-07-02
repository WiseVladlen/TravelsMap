package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IUserRepository
import com.yandex.mapkit.location.Location
import javax.inject.Inject

class UpdateUserLocationInteractor @Inject constructor(private val userRepository: IUserRepository) {

    suspend fun run(location: Location) = userRepository.updateLocation(location)
}