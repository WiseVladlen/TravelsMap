package com.example.travels_map.domain.models

import java.util.Date

data class FeedbackData(
    val text: String,
    val rating: Int,
    val date: Date,
)