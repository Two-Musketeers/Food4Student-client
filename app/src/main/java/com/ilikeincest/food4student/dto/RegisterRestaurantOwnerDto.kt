package com.ilikeincest.food4student.dto

data class RegisterRestaurantOwnerDto(
    val name: String,
    val description: String?,
    val address: String,
    val phoneNumber: String,
    val ownerPhoneNumber: String,
    val latitude: Double,
    val longitude: Double
)
