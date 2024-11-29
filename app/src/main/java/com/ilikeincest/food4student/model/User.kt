package com.ilikeincest.food4student.model

data class User(
    val id: String,
    val phoneNumber: String?,
    val displayName: String?,
    val email: String,
    val role: String,
    val ownedRestaurant: Boolean
)