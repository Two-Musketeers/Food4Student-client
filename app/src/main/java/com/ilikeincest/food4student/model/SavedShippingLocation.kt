package com.ilikeincest.food4student.model

data class SavedShippingLocation(
    val locationType: SavedShippingLocationType,
    val location: String,
    val address: String,
    val receiverName: String,
    val receiverPhone: String,
    val buildingNote: String? = null,
    val otherLocationTypeTitle: String? = null,
    val latitude: Double = 0.0, // TODO
    val longitude: Double = 0.0,
)
