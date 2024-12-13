package com.ilikeincest.food4student.dto

data class CreateOrderItemDto(
    val foodItemId: String,
    val quantity: Int,
    val selectedVariations: List<VariationSelectionDto>?
)
