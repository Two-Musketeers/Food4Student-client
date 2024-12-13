package com.ilikeincest.food4student.dto

data class FoodCategoryDto(
    val id: String,
    val name: String,
    val foodItems: List<FoodItemDto>,
    val restaurantId: String
)
