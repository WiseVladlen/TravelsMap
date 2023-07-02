package com.example.travels_map.domain.repositories

import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.models.FeedbackData
import com.parse.ParseObject

interface IFeedbackRepository {
    suspend fun create(): ParseObject
    suspend fun add(feedback: FeedbackData, place: Place) : Result<Nothing?>

    suspend fun requestFeedbackData(parsePlace: ParseObject)
}