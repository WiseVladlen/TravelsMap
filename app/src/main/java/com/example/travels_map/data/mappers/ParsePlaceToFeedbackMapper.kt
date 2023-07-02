package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.Feedback
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.utils.divide
import com.parse.ParseObject
import javax.inject.Inject

class ParsePlaceToFeedbackMapper @Inject constructor() : IEntityMapper<ParseObject, Feedback> {

    override fun mapEntity(entity: ParseObject): Feedback {
        val feedback = entity.getRelation<ParseObject>(Place.KEY_FEEDBACK).query.first
        val number = feedback.getInt(Feedback.KEY_NUMBER)

        return Feedback(
            id = feedback.objectId,
            rating = feedback.getDouble(Feedback.KEY_RATING).divide(number).toFloat(),
            number = number
        )
    }
}