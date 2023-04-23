package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.models.UserLoginData
import com.example.travels_map.domain.repositories.IUserRepository
import javax.inject.Inject

class LogInInteractor @Inject constructor(private val userRepository: IUserRepository) {

    suspend fun run(user: UserLoginData) = userRepository.logIn(user)
}