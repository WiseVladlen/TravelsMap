package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IUserRepository
import javax.inject.Inject

class LogOutInteractor @Inject constructor(private val userRepository: IUserRepository) {

    suspend fun run() = userRepository.logOut()
}