package com.example.neokotlinui.appointments.model

data class Appointment(
    val id: Long = 0L,
    val patientFullName: String,
    val patientContactNumber: String,
    val patientEmailAddress: String,
    val speciality: String,
    val doctorName: String,
    val appointmentDateTime: Long,
    val status: String
)