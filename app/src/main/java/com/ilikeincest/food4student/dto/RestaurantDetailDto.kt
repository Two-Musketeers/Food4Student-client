package com.ilikeincest.food4student.dto

data class RestaurantDetailDto(
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
    val foodCategories: List<FoodCategoryDto>
)