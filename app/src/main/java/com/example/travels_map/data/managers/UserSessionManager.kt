package com.example.travels_map.data.managers

import com.parse.ParseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet

class UserSessionManager {

    private val checkNotNull get() = ParseUser.getCurrentUser() != null

    private val _isNotNullStateFlow = MutableStateFlow(checkNotNull)
    val isNotNullStateFlow = _isNotNullStateFlow.asStateFlow()

    fun checkForNotNullState() = _isNotNullStateFlow.updateAndGet { checkNotNull }
}