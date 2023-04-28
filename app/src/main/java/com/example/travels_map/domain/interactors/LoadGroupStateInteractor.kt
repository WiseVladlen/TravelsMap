package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IGroupRepository
import javax.inject.Inject

class LoadGroupStateInteractor @Inject constructor(private val groupRepository: IGroupRepository) {

    fun run() = groupRepository.getFlow()
}