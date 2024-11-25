package com.ilikeincest.food4student.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Order(
    val id: String, // To be configured with db apis
    val shopName: String,
    val shopId: String, // For extra lookup
    val createdAt: LocalDateTime,
    val items: List<OrderItem>,
)