package com.example.travels_map.domain.entities

data class User(
    val id: String,
    val username: String,
    val fullName: String,
) {
    companion object {
        const val CLASS_NAME = "_User"

        const val KEY_OBJECT_ID = "objectId"
        const val KEY_USERNAME = "username"
        const val KEY_FULL_NAME = "fullName"
        const val KEY_SELECTED_GROUP_ID = "selectedGroupId"
    }
}