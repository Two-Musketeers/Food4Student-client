package com.ilikeincest.food4student.model

import kotlinx.serialization.Serializable

@Serializable
data class SavedShippingLocation(
    val locationType: SavedShippingLocationType,
    val location: String,
    val address: String,
    val receiverName: String? = null,
    val receiverPhone: String? = null,
    val buildingNote: String? = null,
    val otherLocationTypeTitle: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
