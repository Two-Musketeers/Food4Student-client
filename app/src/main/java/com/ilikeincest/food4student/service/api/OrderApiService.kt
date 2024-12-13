package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.OrderCreateDto
import com.ilikeincest.food4student.dto.OrderDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface OrderApiService {
    // For restaurant owner
    @GET("orders")
    suspend fun getOrders() : Response<List<OrderDto>>
    @GET("orders")
    suspend fun getOrder(
        @Query("id") orderId : String
    ) : Response<OrderDto>
    @DELETE("orders")
    suspend fun deleteOrder(
        @Query("id") orderId : String
    ) : Response<Unit>
    @PUT("orders/{id}/approve-order")
    suspend fun approveOrder(
        @Query("id") orderId : String
    ) : Response<Unit>
    @PUT("orders/{id}/reject-order")
    suspend fun rejectOrder(
        @Query("id") orderId : String
    ) : Response<Unit>
    @PUT("orders/{id}/deliver-order")
    suspend fun deliverOrder(
        @Query("id") orderId : String
    ) : Response<Unit>
    @POST("orders")
    suspend fun createOrder(
        @Body order : OrderCreateDto
    ) : Response<OrderDto>
}