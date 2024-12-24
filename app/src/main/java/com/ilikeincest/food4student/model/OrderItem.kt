package com.ilikeincest.food4student.model

import com.ilikeincest.food4student.dto.VariationSelectionDto

data class OrderItem(
    val id: String,
    val foodName: String,
    val foodDescription: String?,
    val price: Int,
    val quantity: Int,
    val foodItemPhotoUrl: String?,
    val originalFoodItemId: String?,
    val variations: String
)