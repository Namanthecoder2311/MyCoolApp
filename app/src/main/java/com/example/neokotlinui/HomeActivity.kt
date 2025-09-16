package com.example.neokotlinui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.neokotlinuiconverted.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val exitIcon: ImageView = findViewById(R.id.iv_top_right_icon)
        exitIcon.setOnClickListener {
            showExitConfirmationDialog()
        }

        // Navigation for Doctors Card
        val doctorsCard: CardView = findViewById(R.id.card_doctors)
        doctorsCard.setOnClickListener {
            val intent = Intent(this, DoctorsActivity::class.java)
            startActivity(intent)
        }

        // Navigation for Facilities Card
        val facilitiesCard: CardView = findViewById(R.id.card_facilities)
        facilitiesCard.setOnClickListener {
            val intent = Intent(this, FacilitiesActivity::class.java)
            startActivity(intent)
        }

        // Navigation for Specialities Card
        val specialitiesCard: CardView = findViewById(R.id.card_specialities)
        specialitiesCard.setOnClickListener {                                  
            val intent = Intent(this, SpecialitiesActivity::class.java)    
            startActivity(intent)                                              
        }                                                                      

        // Navigation for Book Now Card
        val bookNowCard: CardView = findViewById(R.id.card_book_now) // Added
        bookNowCard.setOnClickListener {                                // Added
            val intent = Intent(this, BookingActivity::class.java)  // Added
            startActivity(intent)                                        // Added
        }                                                                // Added
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit Application")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                finishAffinity()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}