package com.ilikeincest.food4student.model

data class FoodCategory(
    val id: String,
    val name: String,
    val foodItems: List<FoodItem>,
    val restaurantId: String
)