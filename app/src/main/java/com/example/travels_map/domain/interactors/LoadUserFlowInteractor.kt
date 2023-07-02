package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IUserRepository
import javax.inject.Inject

class LoadUserFlowInteractor @Inject constructor(private val userRepository: IUserRepository) {

    fun run() = userRepository.userFlow
}