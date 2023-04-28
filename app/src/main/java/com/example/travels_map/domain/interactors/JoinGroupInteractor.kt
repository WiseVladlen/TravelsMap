package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IGroupRepository
import javax.inject.Inject

class JoinGroupInteractor @Inject constructor(private val groupRepository: IGroupRepository) {

    suspend fun run(query: String) = groupRepository.join(query)
}