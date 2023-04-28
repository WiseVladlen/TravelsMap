package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.User
import com.parse.ParseObject
import javax.inject.Inject

class ParseObjectToUserMapper @Inject constructor() : IEntityMapper<ParseObject, User> {

    override fun mapEntity(entity: ParseObject): User {
        return User(
            id = entity.objectId,
            username = entity.getString(User.KEY_USERNAME) ?: String(),
            fullName = entity.getString(User.KEY_FULL_NAME) ?: String(),
        )
    }
}