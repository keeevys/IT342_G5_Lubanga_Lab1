package com.example.dentalbook.services

import android.content.Context
import com.example.dentalbook.models.*
import com.example.dentalbook.utils.DatabaseConfigParser
import com.example.dentalbook.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ApiService(private val context: Context) {
    
    private val config = DatabaseConfigParser.parseConfig(context)
    private val tokenManager = TokenManager(context)
    
    companion object {
        private const val CONNECT_TIMEOUT = 15000
        private const val READ_TIMEOUT = 15000
    }
    
    suspend fun login(emailOrUsername: String, password: String): AuthResponse = withContext(Dispatchers.IO) {
        val url = URL("${config.baseUrl}${config.loginEndpoint}")
        val connection = url.openConnection() as HttpURLConnection
        
        try {
            connection.apply {
                requestMethod = "POST"
                doOutput = true
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT
                setRequestProperty("Content-Type", "application/json")
            }
            
            val requestBody = JSONObject().apply {
                put("emailOrUsername", emailOrUsername)
                put("password", password)
            }.toString()
            
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(requestBody)
                writer.flush()
            }
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                    reader.readText()
                }
                
                val jsonResponse = JSONObject(response)
                val token = jsonResponse.getString("token")
                val userObj = jsonResponse.getJSONObject("user")
                
                val user = User(
                    userId = userObj.getLong("userId"),
                    fullName = userObj.getString("fullName"),
                    email = userObj.getString("email")
                )
                
                // Save token and user info
                tokenManager.saveToken(token)
                tokenManager.saveUserInfo(user.userId!!, user.fullName, user.email)
                
                AuthResponse(token, user)
            } else {
                throw Exception("Login failed: HTTP $responseCode")
            }
        } finally {
            connection.disconnect()
        }
    }
    
    suspend fun register(fullName: String, email: String, password: String): ApiResponse<User> = withContext(Dispatchers.IO) {
        val url = URL("${config.baseUrl}${config.registerEndpoint}")
        val connection = url.openConnection() as HttpURLConnection
        
        try {
            connection.apply {
                requestMethod = "POST"
                doOutput = true
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT
                setRequestProperty("Content-Type", "application/json")
            }
            
            val requestBody = JSONObject().apply {
                put("fullName", fullName)
                put("email", email)
                put("password", password)
            }.toString()
            
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(requestBody)
                writer.flush()
            }
            
            val responseCode = connection.responseCode
            val response = if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                    reader.readText()
                }
            } else {
                BufferedReader(InputStreamReader(connection.errorStream)).use { reader ->
                    reader.readText()
                }
            }
            
            val jsonResponse = JSONObject(response)
            
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                val userObj = jsonResponse.optJSONObject("user")
                val user = if (userObj != null) {
                    User(
                        userId = userObj.getLong("userId"),
                        fullName = userObj.getString("fullName"),
                        email = userObj.getString("email")
                    )
                } else {
                    null
                }
                
                ApiResponse(
                    success = true,
                    message = jsonResponse.optString("message", "Registration successful"),
                    data = user
                )
            } else {
                ApiResponse(
                    success = false,
                    message = jsonResponse.optString("message", "Registration failed"),
                    data = null
                )
            }
        } finally {
            connection.disconnect()
        }
    }
    
    suspend fun getUserProfile(): User = withContext(Dispatchers.IO) {
        val token = tokenManager.getToken() ?: throw Exception("Not authenticated")
        
        val url = URL("${config.baseUrl}${config.profileEndpoint}")
        val connection = url.openConnection() as HttpURLConnection
        
        try {
            connection.apply {
                requestMethod = "GET"
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT
                setRequestProperty("Authorization", "Bearer $token")
                setRequestProperty("Content-Type", "application/json")
            }
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                    reader.readText()
                }
                
                val jsonResponse = JSONObject(response)
                
                User(
                    userId = jsonResponse.getLong("userId"),
                    fullName = jsonResponse.getString("fullName"),
                    email = jsonResponse.getString("email")
                )
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                tokenManager.clearToken()
                throw Exception("Unauthorized")
            } else {
                throw Exception("Failed to fetch profile: HTTP $responseCode")
            }
        } finally {
            connection.disconnect()
        }
    }
    
    fun logout() {
        tokenManager.clearToken()
    }
    
    fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }
    
    fun getCurrentUser(): User? {
        val userId = tokenManager.getUserId()
        val userName = tokenManager.getUserName()
        val userEmail = tokenManager.getUserEmail()
        
        return if (userId != -1L && userName != null && userEmail != null) {
            User(userId, userName, userEmail)
        } else {
            null
        }
    }
}
