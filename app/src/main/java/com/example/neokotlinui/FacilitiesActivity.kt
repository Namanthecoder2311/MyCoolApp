package com.example.neokotlinui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neokotlinuiconverted.R // Project's R class

class FacilitiesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // We'll create activity_facilities.xml next
        setContentView(R.layout.activity_facilities)
    }
}