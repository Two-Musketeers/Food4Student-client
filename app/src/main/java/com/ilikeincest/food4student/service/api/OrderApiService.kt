package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.order.CreateOrderDto
import com.ilikeincest.food4student.model.Order
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderApiService {
    // For restaurant owner
    @GET("orders/users/pending")
    suspend fun getOrdersPending() : Response<List<Order>>
    @GET("orders/users/approved")
    suspend fun getOrdersApproved() : Response<List<Order>>
    @GET("orders/users/delivered")
    suspend fun getOrdersDelivered() : Response<List<Order>>
    @GET("orders/users/cancelled")
    suspend fun getOrdersCancelled() : Response<List<Order>>
    @GET("orders/{id}")
    suspend fun getOrder(
        @Path("id") orderId : String
    ) : Response<Order>
    @DELETE("orders/{id}")
    suspend fun deleteOrder(
        @Path("id") orderId : String
    ) : Response<Unit>
    @PUT("orders/{id}/approve-order")
    suspend fun approveOrder(
        @Path("id") orderId : String
    ) : Response<Unit>
    @PUT("orders/{id}/cancel-order")
    suspend fun cancelOrder(
        @Path("id") orderId : String
    ) : Response<Unit>
    @PUT("orders/{id}/deliver-order")
    suspend fun deliverOrder(
        @Path("id") orderId : String
    ) : Response<Unit>
    @POST("orders/restaurants/{id}")
    suspend fun createOrder(
        @Path("id") restaurantId: String,
        @Body order: CreateOrderDto
    ) : Response<Order>
}