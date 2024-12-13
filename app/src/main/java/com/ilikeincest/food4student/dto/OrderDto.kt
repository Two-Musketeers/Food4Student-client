package com.ilikeincest.food4student.dto

import kotlinx.datetime.Instant

data class OrderDto(
    val id: String,
    val appUserId: String,
    val restaurantId: String,
    val restaurantName: String,
    val orderItems: List<OrderItemDto>,
    val shippingAddress: OrderShippingAddressDto,
    val createdAt: Instant,
    val totalPrice: Int,
    val note: String?,
    val status: String
)
