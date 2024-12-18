package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivitySplash2Binding

class SplashActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivitySplash2Binding
    private var isNavigated = false // Flag to track navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplash2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Apply insets listener
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set onClickListener for the ENTER button
        binding.button.setOnClickListener {
            if (!isNavigated) { // Check if navigation has already occurred
                navigateToMainActivity()
                isNavigated = true // Set flag to true after navigation
            } else {
                // Optional: Log or debug message to check if this block is executed
                // Log.d("SplashActivity2", "Already navigated, ignoring button click.")
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun enableEdgeToEdge() {
        // Implement edge-to-edge support as needed
        // For example:
        window.decorView.systemUiVisibility = (
                window.decorView.systemUiVisibility
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }
}












