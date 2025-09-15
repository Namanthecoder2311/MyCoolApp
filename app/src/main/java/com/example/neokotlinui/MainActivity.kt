package com.example.neokotlinui // Corrected package

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.neokotlinui.HomeActivity
import com.example.neokotlinuiconverted.R // Added R class import

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Still using activity_welcome for now to fix the immediate error
        setContentView(R.layout.activity_welcome) 

        // Corrected button ID and variable name for clarity
        val getStartedButton: Button = findViewById(R.id.get_started_button) 
        getStartedButton.setOnClickListener {
            // Navigate to next screen
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}