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
    @GET("orders/pending")
    suspend fun getOrdersPending() : Response<List<Order>>
    @GET("orders/approved")
    suspend fun getOrdersApproved() : Response<List<Order>>
    @GET("orders/delivered")
    suspend fun getOrdersDelivered() : Response<List<Order>>
    @GET("orders/cancelled")
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
    @POST("orders")
    suspend fun createOrder(
        @Body order: CreateOrderDto
    ) : Response<Order>
}