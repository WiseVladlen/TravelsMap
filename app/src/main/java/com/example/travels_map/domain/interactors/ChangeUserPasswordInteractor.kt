package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IUserRepository
import javax.inject.Inject

class ChangeUserPasswordInteractor @Inject constructor(private val userRepository: IUserRepository) {

    suspend fun run(password: String) = userRepository.changePassword(password)
}