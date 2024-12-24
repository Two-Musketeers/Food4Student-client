package com.ilikeincest.food4student.dto.order

import com.ilikeincest.food4student.dto.VariationSelectionDto

data class CreateOrderItemDto(
    val foodItemId: String,
    val quantity: Int,
    val selectedVariations: List<VariationSelectionDto>?
)
