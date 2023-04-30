package com.example.travels_map.domain.repositories

import com.example.travels_map.domain.entities.User
import com.example.travels_map.domain.models.UserLoginData
import com.example.travels_map.domain.models.UserRegistrationData
import com.parse.ParseUser

interface IUserRepository {
    fun getCurrentUser(): User?
    fun getCurrentParseUserSafely(): ParseUser?
    suspend fun fetchCurrentUser()
    suspend fun signUp(userData: UserRegistrationData): Result<Nothing?>
    suspend fun logIn(userData: UserLoginData): Result<Nothing?>
    suspend fun logOut(): Result<Nothing?>
    suspend fun editProfile(username: String, fullName: String): Result<Nothing?>
    suspend fun changePassword(password: String): Result<Nothing?>
}