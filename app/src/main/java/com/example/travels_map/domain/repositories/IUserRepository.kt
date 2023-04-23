package com.example.travels_map.domain.repositories

import com.example.travels_map.domain.models.UserLoginData
import com.example.travels_map.domain.models.UserRegistrationData
import com.parse.ParseUser

interface IUserRepository {
    suspend fun getCurrentUserSafely(): ParseUser?
    suspend fun signUp(userData: UserRegistrationData)
    suspend fun logIn(userData: UserLoginData)
    suspend fun logOut()
}