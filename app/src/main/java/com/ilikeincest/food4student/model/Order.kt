package com.ilikeincest.food4student.model

import java.time.LocalDate

data class Order(
    val id: String, // To be configured with db apis
    val shopName: String,
    val shopId: String, // For extra lookup
    val date: LocalDate,
    val items: List<OrderItem>,
)