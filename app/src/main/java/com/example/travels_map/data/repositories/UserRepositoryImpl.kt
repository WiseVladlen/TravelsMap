package com.example.travels_map.data.repositories

import com.example.travels_map.data.managers.UserSessionManager
import com.example.travels_map.data.mappers.IEntityMapper
import com.example.travels_map.domain.models.UserLoginData
import com.example.travels_map.domain.models.UserRegistrationData
import com.example.travels_map.domain.repositories.IUserRepository
import com.parse.ParseUser
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userSessionManager: UserSessionManager,
    private val userToParseUserMapper: IEntityMapper<UserRegistrationData, ParseUser>,
) : IUserRepository {

    override suspend fun getCurrentUserSafely(): ParseUser? {
        if (userSessionManager.checkForNotNullState()) {
            return ParseUser.getCurrentUser()
        }
        return null
    }

    override suspend fun signUp(userData: UserRegistrationData) {
        userToParseUserMapper.mapEntity(userData).signUpInBackground { exception ->
            if (exception == null) {
                userSessionManager.checkForNotNullState()
            } else {
                ParseUser.logOut()
            }
        }
    }

    override suspend fun logIn(userData: UserLoginData) {
        ParseUser.logInInBackground(userData.username, userData.password) { user, exception ->
            if (exception == null && user != null) {
                userSessionManager.checkForNotNullState()
            } else {
                ParseUser.logOut()
            }
        }
    }

    override suspend fun logOut() {
        ParseUser.logOutInBackground { exception ->
            if (exception == null) {
                userSessionManager.checkForNotNullState()
            }
        }
    }
}