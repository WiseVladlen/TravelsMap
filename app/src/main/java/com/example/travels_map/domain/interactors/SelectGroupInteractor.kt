package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.entities.Group
import com.example.travels_map.domain.repositories.IGroupRepository
import javax.inject.Inject

class SelectGroupInteractor @Inject constructor(private val groupRepository: IGroupRepository) {

    suspend fun run(group: Group) = groupRepository.select(group)
}