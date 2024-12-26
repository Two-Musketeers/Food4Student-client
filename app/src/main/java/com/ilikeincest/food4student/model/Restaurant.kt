package com.ilikeincest.food4student.model

data class Restaurant(
    val id: String,
    val isApproved: Boolean,
    val name: String,
    val description: String?,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val logoUrl: String?,
    val bannerUrl: String?,
    val totalRatings: Int,
    val averageRating: Double,
    val phoneNumber: String,
    val isFavorited: Boolean,
    val foodCategories: List<FoodCategory>,
    var distanceInKm: Double = 0.0,
    var estimatedTimeInMinutes: Int = 0,
    val perStarRating: List<Int>, // TODO backend
)