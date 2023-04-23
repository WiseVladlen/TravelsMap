package com.example.travels_map.domain.models

class UserRegistrationData(
    val username: String,
    val fullName: String,
    val password: String,
) {
    companion object {
        const val KEY_USERNAME = "username"
        const val KEY_FULL_NAME = "fullName"
        const val KEY_PASSWORD = "password"
    }
}