package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.Group
import com.example.travels_map.domain.entities.User
import com.parse.ParseObject
import com.parse.ParseUser
import com.parse.ktx.findAll
import javax.inject.Inject

class ParseObjectToGroupMapper @Inject constructor(
    private val parseObjectToUserDataMapper: IEntityMapper<ParseObject, User>,
) : IEntityMapper<ParseObject, Group> {

    override fun mapEntity(entity: ParseObject): Group {
        val participants = entity.getRelation<ParseUser>(Group.KEY_PARTICIPANTS).query.findAll()

        return Group(
            id = entity.objectId,
            name = entity.getString(Group.KEY_NAME) ?: String(),
            participants = participants.map { parseObjectToUserDataMapper.mapEntity(it) },
        )
    }
}