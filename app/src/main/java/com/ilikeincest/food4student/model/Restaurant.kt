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
    val isLiked: Boolean,
    val foodCategories: List<FoodCategory>
)