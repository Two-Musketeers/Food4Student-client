package com.ilikeincest.food4student.dto

data class OrderCreateDto(
    val orderItems: List<CreateOrderItemDto>,
    val shippingInfoId: String,
    val note: String?,
    val restaurantId: String
)
