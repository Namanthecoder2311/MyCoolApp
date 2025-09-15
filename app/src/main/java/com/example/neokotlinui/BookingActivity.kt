package com.example.neokotlinui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neokotlinuiconverted.R // Project's R class

class BookingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // We'll create activity_booking.xml next
        setContentView(R.layout.activity_booking)
    }
}