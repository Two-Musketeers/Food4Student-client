package com.ilikeincest.food4student.dto

import kotlinx.serialization.Serializable

@Serializable
data class VariationSelectionDto(
    val variationId: String,
    val variationOptionIds: List<String>
)
