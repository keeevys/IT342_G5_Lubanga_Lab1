package com.example.dentalbook.models

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String
)
