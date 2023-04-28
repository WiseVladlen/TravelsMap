package com.example.travels_map.data.managers

import com.example.travels_map.domain.common.Result
import com.example.travels_map.domain.entities.Group
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GroupManager {

    private val _groupFlow = MutableSharedFlow<Result<Group>>(1, 1, BufferOverflow.DROP_OLDEST)
    val groupFlow = _groupFlow.asSharedFlow()

    suspend fun emit(result: Result<Group>) = _groupFlow.emit(result)
}