package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.models.FeedbackData
import com.example.travels_map.domain.repositories.IFeedbackRepository
import javax.inject.Inject

class AddFeedbackInteractor @Inject constructor(private val feedbackRepository: IFeedbackRepository) {

    suspend fun run(feedbackData: FeedbackData, place: Place) = feedbackRepository.add(feedbackData, place)
}