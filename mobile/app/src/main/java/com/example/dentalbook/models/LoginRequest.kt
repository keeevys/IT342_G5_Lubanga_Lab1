package com.example.dentalbook.models

data class LoginRequest(
    val emailOrUsername: String,
    val password: String
)
