package com.example.neokotlinui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neokotlinuiconverted.R // Project's R class

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This will point to the new activity_main.xml we'll create next
        setContentView(R.layout.activity_main)
    }
}