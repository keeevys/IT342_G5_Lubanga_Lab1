package com.example.dentalbook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Redirect to LoginActivity (entry point)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}