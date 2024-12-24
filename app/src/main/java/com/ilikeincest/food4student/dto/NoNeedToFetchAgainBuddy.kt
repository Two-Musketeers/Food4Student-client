package com.ilikeincest.food4student.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoNeedToFetchAgainBuddy (
    val Id: String,
    val Distance: Double,
    val TimeAway: Int,
    val IsFavorited: Boolean
)