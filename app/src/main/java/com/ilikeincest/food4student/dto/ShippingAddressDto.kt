package com.ilikeincest.food4student.dto

data class ShippingAddressDto(
    val id: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String?,
    val name: String?
)
