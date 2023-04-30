package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IUserRepository
import javax.inject.Inject

class EditUserProfileInteractor @Inject constructor(private val userRepository: IUserRepository) {

    suspend fun run(username: String, fullName: String) = userRepository.editProfile(username, fullName)
}