package com.example.travels_map.data.repositories

import com.example.travels_map.domain.entities.Feedback
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.models.FeedbackData
import com.example.travels_map.domain.repositories.IFeedbackRepository
import com.example.travels_map.domain.repositories.IReviewRepository
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.coroutines.first
import com.parse.coroutines.getById
import com.parse.coroutines.suspendSave
import javax.inject.Inject

class FeedbackRepositoryImpl @Inject constructor(
    private val reviewRepository: IReviewRepository,
): IFeedbackRepository {

    override suspend fun add(feedback: FeedbackData, place: Place): Result<Nothing?> {
        return runCatching {
            if (place.id == null) throw Exception()

            val parsePlace = ParseQuery<ParseObject>(Place.CLASS_NAME).getById(place.id)

            val parseFeedback = parsePlace.getRelation<ParseObject>(Place.KEY_FEEDBACK).query.first().apply {
                increment(Feedback.KEY_RATING, feedback.rating)
                increment(Feedback.KEY_NUMBER)

                getRelation<ParseObject>(Feedback.KEY_REVIEWS).add(reviewRepository.add(feedback, this))
                suspendSave()
            }

            reviewRepository.requestReviewList(parseFeedback)

            return@runCatching null
        }
    }

    override suspend fun requestFeedbackData(parsePlace: ParseObject) {
        reviewRepository.requestReviewList(parsePlace.getRelation<ParseObject>(Place.KEY_FEEDBACK).query.first())
    }

    override suspend fun create(): ParseObject {
        return ParseObject(Feedback.CLASS_NAME).apply {
            put(Feedback.KEY_RATING, 0)
            put(Feedback.KEY_NUMBER, 0)

            suspendSave()
        }
    }
}