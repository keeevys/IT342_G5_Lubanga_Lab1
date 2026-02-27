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

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var fullNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView
    private lateinit var errorMessage: TextView
    
    private lateinit var apiService: ApiService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        // Initialize API service
        apiService = ApiService(this)
        
        // Initialize views
        fullNameInput = findViewById(R.id.fullNameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        registerButton = findViewById(R.id.registerButton)
        loginLink = findViewById(R.id.loginLink)
        errorMessage = findViewById(R.id.errorMessage)
        
        // Set up click listeners
        registerButton.setOnClickListener {
            handleRegister()
        }
        
        loginLink.setOnClickListener {
            navigateToLogin()
        }
    }
    
    private fun handleRegister() {
        val fullName = fullNameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val confirmPassword = confirmPasswordInput.text.toString().trim()
        
        // Hide previous error
        errorMessage.visibility = View.GONE
        
        // Validation
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields")
            return
        }
        
        if (fullName.length < 3) {
            showError("Full name must be at least 3 characters")
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Please enter a valid email address")
            return
        }
        
        if (password != confirmPassword) {
            showError(getString(R.string.passwords_dont_match))
            return
        }
        
        if (password.length < 6) {
            showError(getString(R.string.password_too_short))
            return
        }
        
        // Disable button and show loading state
        registerButton.isEnabled = false
        registerButton.text = getString(R.string.registering)
        
        // Perform registration
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = apiService.register(fullName, email, password)
                
                if (response.success) {
                    // Navigate to login with success message
                    navigateToLoginWithMessage(getString(R.string.registration_successful))
                } else {
                    showError(response.message ?: getString(R.string.registration_failed))
                }
            } catch (e: Exception) {
                showError(getString(R.string.registration_failed))
            } finally {
                // Re-enable button
                registerButton.isEnabled = true
                registerButton.text = getString(R.string.register_button)
            }
        }
    }
    
    private fun showError(message: String) {
        errorMessage.text = message
        errorMessage.visibility = View.VISIBLE
    }
    
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
    
    private fun navigateToLoginWithMessage(message: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("success_message", message)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}
