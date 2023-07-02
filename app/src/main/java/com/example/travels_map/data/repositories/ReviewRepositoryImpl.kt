package com.example.travels_map.data.repositories

import com.example.travels_map.data.mappers.IEntityMapper
import com.example.travels_map.domain.entities.Feedback
import com.example.travels_map.domain.entities.Review
import com.example.travels_map.domain.models.FeedbackData
import com.example.travels_map.domain.repositories.IReviewRepository
import com.example.travels_map.domain.repositories.IUserRepository
import com.parse.ParseObject
import com.parse.ParseUser
import com.parse.coroutines.suspendFind
import com.parse.coroutines.suspendSave
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val userRepository: IUserRepository,
    private val parseObjectToReviewMapper: IEntityMapper<ParseObject, Review>,
    private val reviewDataToParseFeedbackMapper: IEntityMapper<FeedbackData, ParseObject>,
) : IReviewRepository {

    private val _reviewListFlow = MutableSharedFlow<Result<List<Review>>>(1, 1, BufferOverflow.DROP_OLDEST)

    override val reviewListFlow: SharedFlow<Result<List<Review>>> = _reviewListFlow.asSharedFlow()

    override suspend fun add(feedbackData: FeedbackData, parseFeedback: ParseObject): ParseObject {
        return reviewDataToParseFeedbackMapper.mapEntity(feedbackData).apply {
            getRelation<ParseUser>(Review.KEY_USER).add(userRepository.getCurrentParseUserSafely())
            suspendSave()
        }
    }

    override suspend fun requestReviewList(parseFeedback: ParseObject) {
        val result = runCatching {
            val reviewList = parseFeedback.getRelation<ParseObject>(Feedback.KEY_REVIEWS).query.suspendFind()
            return@runCatching reviewList.map { parseObjectToReviewMapper.mapEntity(it) }
        }

        _reviewListFlow.emit(result)
    }
}