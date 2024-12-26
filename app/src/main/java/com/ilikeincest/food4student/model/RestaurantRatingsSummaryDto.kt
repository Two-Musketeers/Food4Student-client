package com.ilikeincest.food4student.model

import com.ilikeincest.food4student.dto.RatingDto

data class RestaurantRatingsSummaryDto (
    val totalRatings: Int,
    val averageRating: Double,
    val perStarRatings: List<Int>,
    val ratings: List<RatingDto>
)