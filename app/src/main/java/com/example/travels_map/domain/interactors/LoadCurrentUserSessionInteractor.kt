package com.example.travels_map.domain.interactors

import com.example.travels_map.data.managers.UserSessionManager
import javax.inject.Inject

class LoadCurrentUserSessionInteractor @Inject constructor(private val userSessionManager: UserSessionManager) {

    fun run() = userSessionManager
}