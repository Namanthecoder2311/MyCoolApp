package com.example.neokotlinui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neokotlinuiconverted.R // Project's R class

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Using a very basic system theme to avoid our custom theme for now
        // and using the minimal layout we just created.
        setTheme(android.R.style.Theme_Material_Light_NoActionBar) // Basic system theme
        setContentView(R.layout.activity_test)
    }
}