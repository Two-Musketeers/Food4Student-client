package com.ilikeincest.food4student.model

import kotlinx.datetime.Instant

data class Order(
    val id: String,
    val restaurantName: String,
    val restaurantId: String,
    val orderItems: List<OrderItem>,
    val shippingAddress: String,
    val createdAt: Instant,
    val totalPrice: Int,
    val note: String?,
    val status: OrderStatus
)