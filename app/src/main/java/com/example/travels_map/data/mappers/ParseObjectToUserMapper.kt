package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.User
import com.parse.ParseObject
import com.yandex.mapkit.geometry.Point
import javax.inject.Inject

private const val EXCEPTION_USERNAME_VALUE_WAS_NULL = "Username value was null"
private const val EXCEPTION_FULL_NAME_VALUE_WAS_NULL = "FullName value was null"

class ParseObjectToUserMapper @Inject constructor() : IEntityMapper<ParseObject, User> {

    override fun mapEntity(entity: ParseObject): User {
        val username = checkNotNull(entity.getString(User.KEY_USERNAME)) {
            EXCEPTION_USERNAME_VALUE_WAS_NULL
        }

        val fullName = checkNotNull(entity.getString(User.KEY_FULL_NAME)) {
            EXCEPTION_FULL_NAME_VALUE_WAS_NULL
        }

        val parseLocation = entity.getParseGeoPoint(User.KEY_LOCATION)

        val location = if (parseLocation == null) {
            Point(0.0, 0.0)
        } else {
            Point(parseLocation.latitude, parseLocation.longitude)
        }

        return User(
            id = entity.objectId,
            username = username,
            fullName = fullName,
            location = location
        )
    }
}