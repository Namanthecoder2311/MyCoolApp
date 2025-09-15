package com.example.neokotlinui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager // Added
import androidx.recyclerview.widget.RecyclerView // Added
import com.example.neokotlinui.adapter.DoctorsAdapter // Added
import com.example.neokotlinui.model.Doctor // Added
import com.example.neokotlinuiconverted.R
import java.util.UUID // Added for unique IDs

class DoctorsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors)

        val backButton: ImageView = findViewById(R.id.iv_back_button)
        backButton.setOnClickListener {
            finish() // Closes this activity and returns to the previous one
        }

        // Setup RecyclerView
        val doctorsRecyclerView: RecyclerView = findViewById(R.id.rv_doctors_list)
        doctorsRecyclerView.layoutManager = LinearLayoutManager(this)

        val doctorsList = getSampleDoctorsData()
        val doctorsAdapter = DoctorsAdapter(doctorsList)
        doctorsRecyclerView.adapter = doctorsAdapter
    }

    private fun getSampleDoctorsData(): List<Doctor> {
        return listOf(
            Doctor(
                id = UUID.randomUUID().toString(),
                name = "Dr. Sarah Johnson",
                speciality = "Orthodontist",
                experience = "15 years experience",
                imageName = null, // Or "ic_doctor_sarah" if you add a specific drawable
                isAvailable = true
            ),
            Doctor(
                id = UUID.randomUUID().toString(),
                name = "Dr. Michael Chen",
                speciality = "Oral Surgeon",
                experience = "12 years experience",
                imageName = null,
                isAvailable = true
            ),
            Doctor(
                id = UUID.randomUUID().toString(),
                name = "Dr. Emily Rodriguez",
                speciality = "Periodontist",
                experience = "8 years experience",
                imageName = null,
                isAvailable = false // Busy
            ),
            Doctor(
                id = UUID.randomUUID().toString(),
                name = "Dr. James Wilson",
                speciality = "General Dentist",
                experience = "20 years experience",
                imageName = null,
                isAvailable = true
            ),
            Doctor(
                id = UUID.randomUUID().toString(),
                name = "Dr. Linda Karen",
                speciality = "Pediatric Dentist",
                experience = "10 years experience",
                imageName = null,
                isAvailable = false // Busy
            )
        )
    }
}