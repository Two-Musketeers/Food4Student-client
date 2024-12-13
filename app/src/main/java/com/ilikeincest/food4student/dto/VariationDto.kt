package com.ilikeincest.food4student.dto

data class VariationDto(
    val id: String,
    val name: String,
    val minSelect: Int,
    val maxSelect: Int,
    val variationOptions: List<VariationOptionDto>?
)
