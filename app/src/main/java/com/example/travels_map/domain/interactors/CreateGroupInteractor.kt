package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IGroupRepository
import javax.inject.Inject

class CreateGroupInteractor @Inject constructor(private val groupRepository: IGroupRepository) {

    suspend fun run(name: String) = groupRepository.create(name)
}