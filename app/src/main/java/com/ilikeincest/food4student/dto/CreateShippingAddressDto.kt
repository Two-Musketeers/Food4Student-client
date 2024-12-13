package com.ilikeincest.food4student.dto

data class CreateShippingAddressDto(
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String,
    val name: String
)