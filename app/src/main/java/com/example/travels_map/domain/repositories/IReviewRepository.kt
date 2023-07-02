package com.example.travels_map.domain.repositories

import com.example.travels_map.domain.entities.Review
import com.example.travels_map.domain.models.FeedbackData
import com.parse.ParseObject
import kotlinx.coroutines.flow.SharedFlow

interface IReviewRepository {
    val reviewListFlow: SharedFlow<Result<List<Review>>>

    suspend fun add(feedbackData: FeedbackData, parseFeedback: ParseObject) : ParseObject
    suspend fun requestReviewList(parseFeedback: ParseObject)
}