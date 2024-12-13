package com.ilikeincest.food4student.dto

data class FoodItemDto(
    val id: String,
    val name: String,
    val description: String?,
    val foodItemPhotoUrl: String,
    val basePrice: Int,
    val foodItemVariations: List<FoodItemVariationDto>
)
