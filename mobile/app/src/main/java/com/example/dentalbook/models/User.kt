package com.example.dentalbook.models

data class User(
    val userId: Long?,
    val fullName: String,
    val email: String,
    val password: String? = null
)
