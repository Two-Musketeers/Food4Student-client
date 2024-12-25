package com.ilikeincest.food4student.model

import kotlinx.serialization.Serializable

@Serializable
data class FoodItem(
    val id: String,
    val name: String,
    val description: String?,
    val foodItemPhotoUrl: String?,
    val basePrice: Int,
    val variations: List<Variation>
)