package com.example.travels_map.domain.entities

import com.yandex.mapkit.geometry.Point

/**
 * @property isCustomObject defines the type of object creation on the map:
 * existed by default or created manually
 */
data class Place(
    val id: String? = null,
    val name: String? = null,
    val feedback: Feedback? = null,
    val isCustomObject: Boolean = false,
    val coordinates: Point
) {
    companion object {
        const val CLASS_NAME = "Place"

        const val KEY_OBJECT_ID = "objectId"
        const val KEY_NAME = "name"
        const val KEY_FEEDBACK = "feedback"
        const val KEY_COORDINATES = "coordinates"
        const val KEY_IS_CUSTOM_OBJECT = "isCustomObject"
    }
}

data class Feedback(
    val id: String,
    val rating: Float = 0f,
    val number: Int = 0,
) {
    companion object {
        const val CLASS_NAME = "Feedback"

        const val KEY_REVIEWS = "reviews"
        const val KEY_RATING = "rating"
        const val KEY_NUMBER = "number"
    }
}