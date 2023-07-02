package com.example.travels_map.domain.entities

import java.util.Date

data class Review(
    val id: String,
    val text: String,
    val rating: Int,
    val date: Date,
    val user: User,
) {
    companion object {
        const val CLASS_NAME = "Review"

        const val KEY_TEXT = "text"
        const val KEY_RATING = "rating"
        const val KEY_DATE = "date"
        const val KEY_USER = "user"
    }
}