package com.example.dentalbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dentalbook.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    
    private lateinit var emailOrUsernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView
    private lateinit var errorMessage: TextView
    private lateinit var successMessage: TextView
    
    private lateinit var apiService: ApiService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // Initialize API service
        apiService = ApiService(this)
        
        // Check if user is already logged in
        if (apiService.isLoggedIn()) {
            navigateToDashboard()
            return
        }
        
        // Initialize views
        emailOrUsernameInput = findViewById(R.id.emailOrUsernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)
        errorMessage = findViewById(R.id.errorMessage)
        successMessage = findViewById(R.id.successMessage)
        
        // Show success message if coming from registration
        val message = intent.getStringExtra("success_message")
        if (message != null) {
            successMessage.text = message
            successMessage.visibility = View.VISIBLE
        }
        
        // Set up click listeners
        loginButton.setOnClickListener {
            handleLogin()
        }
        
        registerLink.setOnClickListener {
            navigateToRegister()
        }
    }
    
    private fun handleLogin() {
        val emailOrUsername = emailOrUsernameInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        
        // Hide previous messages
        errorMessage.visibility = View.GONE
        successMessage.visibility = View.GONE
        
        // Validation
        if (emailOrUsername.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields")
            return
        }
        
        // Disable button and show loading state
        loginButton.isEnabled = false
        loginButton.text = getString(R.string.logging_in)
        
        // Perform login
        CoroutineScope(Dispatchers.Main).launch {
            try {
                apiService.login(emailOrUsername, password)
                
                // Navigate to dashboard
                navigateToDashboard()
            } catch (e: Exception) {
                showError(getString(R.string.invalid_credentials))
            } finally {
                // Re-enable button
                loginButton.isEnabled = true
                loginButton.text = getString(R.string.login_button)
            }
        }
    }
    
    private fun showError(message: String) {
        errorMessage.text = message
        errorMessage.visibility = View.VISIBLE
    }
    
    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    
    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
