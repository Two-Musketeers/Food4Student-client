package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.OrderCreateDto
import com.ilikeincest.food4student.dto.OrderDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApiService {
    // For restaurant owner
    @GET("orders")
    suspend fun getOrders() : Response<List<OrderDto>>
    @GET("orders/{id}")
    suspend fun getOrder(
        @Path("id") orderId : String
    ) : Response<OrderDto>
    @DELETE("orders/{id}")
    suspend fun deleteOrder(
        @Path("id") orderId : String
    ) : Response<Unit>
    @PUT("orders/{id}/approve-order")
    suspend fun approveOrder(
        @Path("id") orderId : String
    ) : Response<Unit>
    @PUT("orders/{id}/reject-order")
    suspend fun rejectOrder(
        @Path("id") orderId : String
    ) : Response<Unit>
    @PUT("orders/{id}/deliver-order")
    suspend fun deliverOrder(
        @Path("id") orderId : String
    ) : Response<Unit>
    @POST("orders")
    suspend fun createOrder(
        @Body order : OrderCreateDto
    ) : Response<OrderDto>
}