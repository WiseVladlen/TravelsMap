package com.example.travels_map.domain.repositories

import com.example.travels_map.domain.common.Result
import com.example.travels_map.domain.entities.Group
import com.example.travels_map.domain.entities.User
import kotlinx.coroutines.flow.SharedFlow

interface IGroupRepository {
    val participantsLocationFlow: SharedFlow<kotlin.Result<List<User>>>
    fun getFlow(): SharedFlow<Result<Group>>

    suspend fun load()
    suspend fun loadAll(): Result<List<Group>>
    suspend fun select(group: Group)
    suspend fun join(query: String)
    suspend fun leave()
    suspend fun loadKey(): Result<String>
    suspend fun create(name: String)
    suspend fun requestParticipantsLocation()
}