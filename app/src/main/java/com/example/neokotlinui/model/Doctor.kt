package com.example.neokotlinui.model

data class Doctor(
    val id: String, // Or Int, depending on your data source
    val name: String,
    val speciality: String,
    val experience: String, // e.g., "15 years experience"
    val imageName: String?, // For a local drawable resource name, or null
    val isAvailable: Boolean
)