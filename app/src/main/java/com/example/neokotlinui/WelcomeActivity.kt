package com.example.neokotlinui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.neokotlinuiconverted.R // Added correct R class import

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Corrected the ID to R.id.get_started_button
        val getStartedButton: Button = findViewById(R.id.get_started_button)
        getStartedButton.setOnClickListener {
            // Navigate to HomeActivity as per your last code
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}