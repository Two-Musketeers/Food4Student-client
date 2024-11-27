package com.ilikeincest.food4student.model

data class Restaurant(
    val id: String,
    val name: String,
    val description: String?,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val logo: String?,
    val banner: String?,
    val menu: List<String>,
    val likedByUsers: List<String>,
    val ratings: List<String>,
    val orders: List<String>,
    val isApproved: Boolean,
    val averageRating: Double
)