package com.ilikeincest.food4student.model

import kotlinx.serialization.Serializable

@Serializable
data class VariationOption(
    val id: String,
    val name: String,
    val priceAdjustment: Int
)