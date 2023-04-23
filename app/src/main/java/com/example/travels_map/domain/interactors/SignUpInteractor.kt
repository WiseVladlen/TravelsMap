package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.models.UserRegistrationData
import com.example.travels_map.domain.repositories.IUserRepository
import javax.inject.Inject

class SignUpInteractor @Inject constructor(private val userRepository: IUserRepository) {

    suspend fun run(user: UserRegistrationData) = userRepository.signUp(user)
}