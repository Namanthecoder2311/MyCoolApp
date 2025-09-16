package com.example.neokotlinui.model

data class Speciality(
    val id: String, // Or Int
    val name: String,
    val description: String,
    val imageName: String? = null // To hold the drawable name for the main image
)