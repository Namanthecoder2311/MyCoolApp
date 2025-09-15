package com.example.neokotlinui.model

data class Facility(
    val id: String, // Or Int
    val name: String,
    val description: String,
    val imageName: String // Name of the drawable resource for the image
)