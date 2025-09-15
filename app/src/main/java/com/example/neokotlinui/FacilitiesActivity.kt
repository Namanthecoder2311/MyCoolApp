package com.example.neokotlinui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager // Added
import androidx.recyclerview.widget.RecyclerView
import com.example.neokotlinui.adapter.FacilitiesAdapter
import com.example.neokotlinui.model.Facility
import com.example.neokotlinuiconverted.R
import java.util.UUID

class FacilitiesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facilities) // Set the correct layout

        val backButton: ImageView = findViewById(R.id.iv_facility_back_button)
        backButton.setOnClickListener {
            finish() // Closes this activity
        }

        // Setup RecyclerView
        val facilitiesRecyclerView: RecyclerView = findViewById(R.id.rv_facilities_list)
        // The XML already defines GridLayoutManager with spanCount=2,
        // but setting it here programmatically is also fine and explicit.
        facilitiesRecyclerView.layoutManager = GridLayoutManager(this, 2)

        val facilitiesList = getSampleFacilitiesData()
        val facilitiesAdapter = FacilitiesAdapter(facilitiesList)
        facilitiesRecyclerView.adapter = facilitiesAdapter
    }

    private fun getSampleFacilitiesData(): List<Facility> {
        return listOf(
            Facility(
                id = UUID.randomUUID().toString(),
                name = "Examination Rooms",
                description = "State-of-the-art dental chairs with advanced diagnostic equipment",
                imageName = "img_examination_rooms" // Ensure this drawable exists in res/drawable
            ),
            Facility(
                id = UUID.randomUUID().toString(),
                name = "Surgery Suite",
                description = "Sterile surgical environment with latest medical technology",
                imageName = "img_surgery_suite" // Ensure this drawable exists
            ),
            Facility(
                id = UUID.randomUUID().toString(),
                name = "Digital Imaging",
                description = "Advanced X-ray and 3D imaging technology for precise diagnosis",
                imageName = "img_digital_imaging" // Ensure this drawable exists
            ),
            Facility(
                id = UUID.randomUUID().toString(),
                name = "Reception Area",
                description = "Comfortable waiting area designed for patient relaxation",
                imageName = "img_reception_area" // Ensure this drawable exists
            )
        )
    }
}