package com.ilikeincest.food4student.dto

data class OrderItemDto(
    val id: String,
    val foodName: String,
    val foodDescription: String?,
    val price: Int,
    val quantity: Int,
    val foodItemPhotoUrl: String?,
    val originalFoodItemId: String?,
    val selectedVariations: List<OrderItemVariationDto>
)
