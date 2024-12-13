package com.ilikeincest.food4student.dto

data class RestaurantRegisterDto(
    val name: String,
    val description: String,
    val address: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val latitude: Double,
    val longitude: Double
)
