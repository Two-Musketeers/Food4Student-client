package com.ilikeincest.food4student.dto.order

import com.ilikeincest.food4student.dto.VariationSelectionDto
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderItemDto(
    val foodItemId: String,
    val quantity: Int,
    val selectedVariations: List<VariationSelectionDto>?
)
