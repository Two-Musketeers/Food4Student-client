package com.ilikeincest.food4student.model

import kotlinx.serialization.Serializable

@Serializable
data class SavedShippingLocation(
    val id: String,
    val locationType: SavedShippingLocationType,
    val address: String,
    val location: String = address,
    val name: String? = null,
    val phoneNumber: String? = null,
    val buildingNote: String? = null, // consider this note
    val otherLocationTypeTitle: String? = null,
    val latitude: Double,
    val longitude: Double
)
