package com.example.dentalbook.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "DentalBookPrefs"
        private const val TOKEN_KEY = "auth_token"
        private const val USER_ID_KEY = "user_id"
        private const val USER_NAME_KEY = "user_name"
        private const val USER_EMAIL_KEY = "user_email"
    }

    fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun saveUserInfo(userId: Long, fullName: String, email: String) {
        prefs.edit().apply {
            putLong(USER_ID_KEY, userId)
            putString(USER_NAME_KEY, fullName)
            putString(USER_EMAIL_KEY, email)
            apply()
        }
    }

    fun getUserId(): Long {
        return prefs.getLong(USER_ID_KEY, -1)
    }

    fun getUserName(): String? {
        return prefs.getString(USER_NAME_KEY, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL_KEY, null)
    }

    fun clearToken() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}
