package com.example.neokotlinui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neokotlinuiconverted.R // Project's R class

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // No explicit setTheme() call, so it will use the manifest's theme
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        android.util.Log.d("TestActivity", "onCreate completed with XML setContentView and application theme.")
    }
}