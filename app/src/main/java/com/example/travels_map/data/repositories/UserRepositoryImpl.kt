package com.example.travels_map.data.repositories

import com.example.travels_map.data.mappers.IEntityMapper
import com.example.travels_map.domain.entities.User
import com.example.travels_map.domain.models.UserLoginData
import com.example.travels_map.domain.models.UserRegistrationData
import com.example.travels_map.domain.repositories.IUserRepository
import com.parse.ParseGeoPoint
import com.parse.ParseObject
import com.parse.ParseUser
import com.parse.coroutines.parseLogIn
import com.parse.coroutines.suspendFetch
import com.parse.coroutines.suspendSave
import com.parse.coroutines.suspendSignUp
import com.yandex.mapkit.location.Location
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRegistrationDataToParseUserMapper: IEntityMapper<UserRegistrationData, ParseUser>,
    private val parseObjectToUserMapper: IEntityMapper<ParseObject, User>,
) : IUserRepository {

    private val _userFlow = MutableSharedFlow<Result<User>>(1, 1, BufferOverflow.DROP_OLDEST)

    override val userFlow: SharedFlow<Result<User>> = _userFlow.asSharedFlow()

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
        _userFlow.emit(result)
    }

    override suspend fun signUp(userData: UserRegistrationData): Result<Nothing?> {
        return runCatching {
            val parseUser = userRegistrationDataToParseUserMapper.mapEntity(userData)
            parseUser.suspendSignUp()

            val user = parseObjectToUserMapper.mapEntity(ParseUser.getCurrentUser())
            _userFlow.emit(Result.success(user))

            return@runCatching null
        }
    }

    override suspend fun logIn(userData: UserLoginData): Result<Nothing?> {
        return runCatching {
            val user = parseLogIn(userData.username, userData.password)

            _userFlow.emit(Result.success(parseObjectToUserMapper.mapEntity(user)))

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

            _userFlow.emit(Result.success(parseObjectToUserMapper.mapEntity(user)))

            return@runCatching null
        }
    }

    override suspend fun changePassword(password: String): Result<Nothing?> {
        return runCatching {
            val user = ParseUser.getCurrentUser().apply {
                setPassword(password)
                suspendSave()
            }

            _userFlow.emit(Result.success(parseObjectToUserMapper.mapEntity(user)))

            return@runCatching null
        }
    }

    override suspend fun updateLocation(location: Location): Result<Nothing?> {
        return runCatching {
            ParseUser.getCurrentUser().apply {
                put(User.KEY_LOCATION, ParseGeoPoint(location.position.latitude, location.position.longitude))
                suspendSave()
            }

            return@runCatching null
        }
    }
}