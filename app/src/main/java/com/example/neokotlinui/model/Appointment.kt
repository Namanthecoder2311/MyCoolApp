package com.example.neokotlinui.model

import java.util.UUID

data class Appointment(
    val id: String = UUID.randomUUID().toString(), // Auto-generated unique ID
    val fullName: String,
    val contactNumber: String,
    val emailAddress: String,
    val medicalConcern: String?, // Nullable if it's optional
    val speciality: String,
    val preferredDoctor: String,
    val preferredDate: String, // Store as String, or convert to a Date/Long
    val preferredTime: String, // Store as String, or convert
    val bookingTimestamp: Long = System.currentTimeMillis() // Record when it was booked
)