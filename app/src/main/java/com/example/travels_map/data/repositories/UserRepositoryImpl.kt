package com.example.travels_map.data.repositories

import com.example.travels_map.data.managers.UserManager
import com.example.travels_map.data.mappers.IEntityMapper
import com.example.travels_map.domain.entities.User
import com.example.travels_map.domain.models.UserLoginData
import com.example.travels_map.domain.models.UserRegistrationData
import com.example.travels_map.domain.repositories.IUserRepository
import com.parse.ParseObject
import com.parse.ParseUser
import com.parse.coroutines.parseLogIn
import com.parse.coroutines.suspendFetch
import com.parse.coroutines.suspendSave
import com.parse.coroutines.suspendSignUp
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userManager: UserManager,
    private val userRegistrationDataToParseUserMapper: IEntityMapper<UserRegistrationData, ParseUser>,
    private val parseObjectToUserMapper: IEntityMapper<ParseObject, User>,
) : IUserRepository {

    override fun getCurrentUser(): User? {
        return getCurrentParseUserSafely()?.let { user ->
            parseObjectToUserMapper.mapEntity(user)
        }
    }

    override fun getCurrentParseUserSafely(): ParseUser? {
        return ParseUser.getCurrentUser()
    }

    override suspend fun fetchCurrentUser() {
        val result = runCatching {
            parseObjectToUserMapper.mapEntity(ParseUser.getCurrentUser().suspendFetch())
        }
        userManager.emit(result)
    }

    override suspend fun signUp(userData: UserRegistrationData): Result<Nothing?> {
        return runCatching {
            val parseUser = userRegistrationDataToParseUserMapper.mapEntity(userData)
            parseUser.suspendSignUp()

            val user = parseObjectToUserMapper.mapEntity(ParseUser.getCurrentUser())
            userManager.emit(Result.success(user))

            return@runCatching null
        }
    }

    override suspend fun logIn(userData: UserLoginData): Result<Nothing?> {
        return runCatching {
            val user = parseLogIn(userData.username, userData.password)

            userManager.emit(Result.success(parseObjectToUserMapper.mapEntity(user)))

            return@runCatching null
        }
    }

    override suspend fun logOut(): Result<Nothing?> {
        return runCatching {
            ParseUser.logOut()

            return@runCatching null
        }
    }

    override suspend fun editProfile(username: String, fullName: String): Result<Nothing?> {
        return runCatching {
            val user = ParseUser.getCurrentUser().apply {
                put(User.KEY_USERNAME, username)
                put(User.KEY_FULL_NAME, fullName)
                suspendSave()
            }

            userManager.emit(Result.success(parseObjectToUserMapper.mapEntity(user)))

            return@runCatching null
        }
    }

    override suspend fun changePassword(password: String): Result<Nothing?> {
        return runCatching {
            val user = ParseUser.getCurrentUser().apply {
                setPassword(password)
                suspendSave()
            }

            userManager.emit(Result.success(parseObjectToUserMapper.mapEntity(user)))

            return@runCatching null
        }
    }
}