package com.example.travels_map.domain.entities

data class Group(
    val id: String,
    val name: String,
    val participants: List<User>,
) {
    companion object {
        const val CLASS_NAME = "Group"

        const val KEY_OBJECT_ID = "objectId"
        const val KEY_NAME = "name"
        const val KEY_PARTICIPANTS = "participants"
    }
}