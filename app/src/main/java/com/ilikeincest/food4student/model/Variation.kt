package com.ilikeincest.food4student.model

import kotlinx.serialization.Serializable

@Serializable
data class Variation(
    val id: String,
    val name: String,
    val minSelect: Int,
    val maxSelect: Int?,
    val variationOptions: List<VariationOption>
)