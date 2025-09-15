package com.example.neokotlinui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager // Added
import androidx.recyclerview.widget.RecyclerView       // Added
import com.example.neokotlinui.adapter.SpecialitiesAdapter // Added
import com.example.neokotlinui.model.Speciality          // Added
import com.example.neokotlinuiconverted.R
import java.util.UUID                                   // Added

class SpecialitiesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specialities)

        val backButton: ImageView = findViewById(R.id.iv_speciality_back_button)
        backButton.setOnClickListener {
            finish() // Closes this activity
        }

        // Setup RecyclerView
        val specialitiesRecyclerView: RecyclerView = findViewById(R.id.rv_specialities_list)
        specialitiesRecyclerView.layoutManager = LinearLayoutManager(this)

        val specialitiesList = getSampleSpecialitiesData()
        val specialitiesAdapter = SpecialitiesAdapter(specialitiesList)
        specialitiesRecyclerView.adapter = specialitiesAdapter
    }

    private fun getSampleSpecialitiesData(): List<Speciality> {
        return listOf(
            Speciality(
                id = UUID.randomUUID().toString(),
                name = "General Dentistry",
                description = "Routine check-ups, cleanings, and preventive care"
            ),
            Speciality(
                id = UUID.randomUUID().toString(),
                name = "Orthodontics",
                description = "Braces, aligners, and teeth straightening treatments"
            ),
            Speciality(
                id = UUID.randomUUID().toString(),
                name = "Oral Surgery",
                description = "Tooth extractions, implants, and surgical procedures"
            ),
            Speciality(
                id = UUID.randomUUID().toString(),
                name = "Periodontics",
                description = "Gum disease treatment and periodontal care"
            ),
            Speciality(
                id = UUID.randomUUID().toString(),
                name = "Cosmetic Dentistry",
                description = "Veneers, whitening, and smile makeovers"
            ),
            Speciality(
                id = UUID.randomUUID().toString(),
                name = "Pediatric Dentistry",
                description = "Specialized dental care for children and adolescents"
            )
        )
    }
}