package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.CreateRatingDto
import com.ilikeincest.food4student.dto.CreateShippingAddressDto
import com.ilikeincest.food4student.dto.OrderDto
import com.ilikeincest.food4student.dto.RatingDto
import com.ilikeincest.food4student.dto.ShippingAddressDto
import com.ilikeincest.food4student.model.Notification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @GET("lmao")
    suspend fun getLmao() : Response<String>
    // Notifications
    @GET("users/notifications")
    suspend fun getNotifications() : Response<List<Notification>>
    @POST("users/notifications")
    suspend fun readNotifications() : Response<Unit>
    @POST("users/notifications/{id}")
    suspend fun readNotification(
        @Path("id") id: String
    ) : Response<Unit>
    @PUT("users/notifications/{id}")
    suspend fun unReadNotification(
        @Path("id") id: String
    ) : Response<Unit>
    @DELETE("users/notifications")
    suspend fun deleteNotifications() : Response<Unit>
    @DELETE("users/notifications/{id}")
    suspend fun deleteNotification(
        @Path("id") id: String
    ) : Response<Unit>
    // Shipping Addresses
    @POST("users/shipping-addresses")
    suspend fun addShippingAddress(@Body shippingAddress: CreateShippingAddressDto) : Response<ShippingAddressDto>
    @PUT("users/shipping-addresses")
    suspend fun updateShippingAddress(
        @Query("id") id: String,
        @Body shippingAddress: ShippingAddressDto
    ) : Response<ShippingAddressDto>
    @GET("users/shipping-addresses")
    suspend fun getShippingAddresses() : Response<List<ShippingAddressDto>>
    @GET("users/shipping-addresses")
    suspend fun getShippingAddress(
        @Query("id") id: String
    ) : Response<ShippingAddressDto>
    @DELETE("users/shipping-addresses")
    suspend fun deleteShippingAddress(
        @Query("id") id: String
    ) : Response<Unit>
    // Like a restaurant
    @POST("users/likes")
    suspend fun likeRestaurant(
        @Query("restaurantId") restaurantId: String
    ) : Response<Unit>
    // Rating a restaurant
    @POST("users/ratings")
    suspend fun rateRestaurant(
        @Query("orderId") orderId: String,
        @Body createRatingDto: CreateRatingDto
    ) : Response<Unit>
    @GET("users/ratings")
    suspend fun getRatings() : Response<List<RatingDto>>
    @GET("users/ratings")
    suspend fun getRating(
        @Query("id") id: String
    ) : Response<RatingDto>
    // Orders
    @GET("users/orders")
    suspend fun getOrders() : Response<List<OrderDto>>
    @GET("users/orders")
    suspend fun getOrder(
        @Query("id") orderId: String
    ) : Response<OrderDto>
    @DELETE("orders")
    suspend fun deleteOrder(
        @Query("id") orderId: String
    ) : Response<Unit>
}