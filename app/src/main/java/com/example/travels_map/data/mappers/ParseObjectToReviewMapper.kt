package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.Review
import com.example.travels_map.domain.entities.User
import com.parse.ParseObject
import com.parse.ParseUser
import javax.inject.Inject

private const val EXCEPTION_TEXT_VALUE_WAS_NULL = "Text value was null"
private const val EXCEPTION_RATING_VALUE_WAS_NULL = "Rating value was null"
private const val EXCEPTION_DATE_VALUE_WAS_NULL = "Date value was null"

class ParseObjectToReviewMapper @Inject constructor(
    private val parseObjectToUserMapper: IEntityMapper<ParseObject, User>,
) : IEntityMapper<ParseObject, Review> {

    override fun mapEntity(entity: ParseObject): Review {
        val text = checkNotNull(entity.getString(Review.KEY_TEXT)) { EXCEPTION_TEXT_VALUE_WAS_NULL }
        val rating = checkNotNull(entity.getInt(Review.KEY_RATING)) { EXCEPTION_RATING_VALUE_WAS_NULL }
        val date = checkNotNull(entity.getDate(Review.KEY_DATE)) { EXCEPTION_DATE_VALUE_WAS_NULL }
        val parseUser = entity.getRelation<ParseUser>(Review.KEY_USER).query.first

        return Review(
            id = entity.objectId,
            text = text,
            rating = rating,
            date = date,
            user = parseObjectToUserMapper.mapEntity(parseUser)
        )
    }
}