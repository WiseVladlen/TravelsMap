package com.example.travels_map.domain.interactors

import com.example.travels_map.data.managers.UserManager
import javax.inject.Inject

class LoadUserFlowInteractor @Inject constructor(private val userManager: UserManager) {

    fun run() = userManager.userFlow
}