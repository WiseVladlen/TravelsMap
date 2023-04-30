package com.example.travels_map.data.managers

import com.example.travels_map.domain.entities.User
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class UserManager {

    private val _userFlow = MutableSharedFlow<Result<User>>(1, 1, BufferOverflow.DROP_OLDEST)
    val userFlow = _userFlow.asSharedFlow()

    suspend fun emit(result: Result<User>) = _userFlow.emit(result)
}