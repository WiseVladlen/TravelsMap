package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.User
import com.example.travels_map.domain.models.UserRegistrationData
import com.parse.ParseGeoPoint
import com.parse.ParseUser
import javax.inject.Inject

class UserRegistrationDataToParseUserMapper @Inject constructor() : IEntityMapper<UserRegistrationData, ParseUser> {

    override fun mapEntity(entity: UserRegistrationData): ParseUser {
        return ParseUser().apply {
            put(UserRegistrationData.KEY_USERNAME, entity.username)
            put(UserRegistrationData.KEY_FULL_NAME, entity.fullName)
            put(UserRegistrationData.KEY_PASSWORD, entity.password)

            put(User.KEY_LOCATION, ParseGeoPoint(0.0, 0.0))
        }
    }
}