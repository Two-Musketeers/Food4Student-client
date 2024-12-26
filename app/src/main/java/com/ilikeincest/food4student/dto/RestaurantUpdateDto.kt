package com.ilikeincest.food4student.dto

data class RestaurantUpdateDto (
    val name: String,
    val description: String?,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String,
)