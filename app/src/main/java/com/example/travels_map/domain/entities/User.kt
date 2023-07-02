package com.example.travels_map.domain.entities

import com.yandex.mapkit.geometry.Point

data class User(
    val id: String,
    val username: String,
    val fullName: String,
    val location: Point
) {
    companion object {
        const val CLASS_NAME = "_User"

        const val KEY_OBJECT_ID = "objectId"
        const val KEY_USERNAME = "username"
        const val KEY_FULL_NAME = "fullName"
        const val KEY_LOCATION = "location"
        const val KEY_SELECTED_GROUP_ID = "selectedGroupId"
    }
}