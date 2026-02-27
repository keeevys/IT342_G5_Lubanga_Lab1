package com.example.dentalbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dentalbook.models.User
import com.example.dentalbook.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    
    private lateinit var logoutButton: Button
    private lateinit var errorMessage: TextView
    private lateinit var loadingText: TextView
    private lateinit var profileCard: View
    private lateinit var userIdValue: TextView
    private lateinit var fullNameValue: TextView
    private lateinit var emailValue: TextView
    
    private lateinit var apiService: ApiService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        
        // Initialize API service
        apiService = ApiService(this)
        
        // Check if user is logged in
        if (!apiService.isLoggedIn()) {
            navigateToLogin()
            return
        }
        
        // Initialize views
        logoutButton = findViewById(R.id.logoutButton)
        errorMessage = findViewById(R.id.errorMessage)
        loadingText = findViewById(R.id.loadingText)
        profileCard = findViewById(R.id.profileCard)
        userIdValue = findViewById(R.id.userIdValue)
        fullNameValue = findViewById(R.id.fullNameValue)
        emailValue = findViewById(R.id.emailValue)
        
        // Set up click listeners
        logoutButton.setOnClickListener {
            handleLogout()
        }
        
        // Load user profile
        loadUserProfile()
    }
    
    private fun loadUserProfile() {
        // Show loading state
        loadingText.visibility = View.VISIBLE
        profileCard.visibility = View.GONE
        errorMessage.visibility = View.GONE
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // First try to get cached user data
                val cachedUser = apiService.getCurrentUser()
                if (cachedUser != null) {
                    displayUserProfile(cachedUser)
                }
                
                // Then fetch fresh data from API
                val user = apiService.getUserProfile()
                displayUserProfile(user)
            } catch (e: Exception) {
                // If API call fails but we have cached data, continue showing it
                val cachedUser = apiService.getCurrentUser()
                if (cachedUser != null) {
                    displayUserProfile(cachedUser)
                } else {
                    showError(getString(R.string.failed_to_load_profile))
                    
                    // If unauthorized, navigate to login
                    if (e.message == "Unauthorized") {
                        navigateToLogin()
                    }
                }
            } finally {
                loadingText.visibility = View.GONE
            }
        }
    }
    
    private fun displayUserProfile(user: User) {
        userIdValue.text = user.userId.toString()
        fullNameValue.text = user.fullName
        emailValue.text = user.email
        
        profileCard.visibility = View.VISIBLE
        loadingText.visibility = View.GONE
    }
    
    private fun showError(message: String) {
        errorMessage.text = message
        errorMessage.visibility = View.VISIBLE
        loadingText.visibility = View.GONE
    }
    
    private fun handleLogout() {
        apiService.logout()
        navigateToLogin()
    }
    
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
