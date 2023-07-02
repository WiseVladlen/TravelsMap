package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.Review
import com.example.travels_map.domain.models.FeedbackData
import com.parse.ParseObject
import javax.inject.Inject

class FeedbackDataToParseReviewMapper @Inject constructor() : IEntityMapper<FeedbackData, ParseObject> {

    override fun mapEntity(entity: FeedbackData): ParseObject {
        return ParseObject(Review.CLASS_NAME).apply {
            put(Review.KEY_TEXT, entity.text)
            put(Review.KEY_RATING, entity.rating)
            put(Review.KEY_DATE, entity.date)
        }
    }
}