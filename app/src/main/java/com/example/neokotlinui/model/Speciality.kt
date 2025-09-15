package com.example.neokotlinui.model

data class Speciality(
    val id: String, // Or Int
    val name: String,
    val description: String,
    val iconPlaceholder: String? = null // For potential future icon, not strictly needed for current design
)