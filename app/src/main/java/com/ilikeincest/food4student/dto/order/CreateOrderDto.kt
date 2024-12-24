package com.ilikeincest.food4student.dto.order

data class CreateOrderDto(
    val orderItems: List<CreateOrderItemDto>,
    val shippingAddress: String,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String,
    val name: String,
    val note: String?,
)
