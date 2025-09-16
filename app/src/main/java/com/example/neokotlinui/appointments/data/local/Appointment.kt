package com.example.neokotlinui.appointments.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointment")
data class Appointment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientFullName: String,
    val patientContactNumber: String,
    val patientEmailAddress: String,
    val doctorName: String,
    val speciality: String,
    val appointmentDateTime: Long, // Epoch time, single source of truth for date/time
    val status: String,
    val notes: String?
    // 'date: String' and 'time: String' have been removed
)