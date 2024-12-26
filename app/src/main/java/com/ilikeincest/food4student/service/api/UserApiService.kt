package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.order.CreateRatingDto
import com.ilikeincest.food4student.dto.RatingDto
import com.ilikeincest.food4student.model.Notification
import com.ilikeincest.food4student.model.Order
import com.ilikeincest.food4student.model.Restaurant
import com.ilikeincest.food4student.model.SavedShippingLocation
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @GET("users/phone")
    suspend fun getPhoneNumber() : Response<ResponseBody>
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
    suspend fun addShippingAddress(
        @Body shippingAddress: SavedShippingLocation
    ) : Response<SavedShippingLocation>
    @PUT("users/shipping-addresses/{id}")
    suspend fun updateShippingAddress(
        @Path("id") id: String,
        @Body shippingAddress: SavedShippingLocation
    ) : Response<SavedShippingLocation>
    @GET("users/shipping-addresses")
    suspend fun getShippingAddresses() : Response<List<SavedShippingLocation>>
    // Don't know if this is needed
    @GET("users/shipping-addresses/{id}")
    suspend fun getShippingAddress(
        @Path("id") id: String
    ) : Response<SavedShippingLocation>
    @DELETE("users/shipping-addresses/{id}")
    suspend fun deleteShippingAddress(
        @Path("id") id: String
    ) : Response<Unit>
    @GET("users/likes")
    suspend fun getLikes(
        @Query("PageNumber") page: Int,
        @Query("PageSize") size: Int
    ) : Response<List<Restaurant>>
    // Like a restaurant
    @POST("users/likes/{restaurantId}")
    suspend fun toggleLikeRestaurant(
        @Path("restaurantId") restaurantId: String
    ) : Response<Unit>
    // Rating a restaurant
    @POST("users/ratings/{orderId}")
    suspend fun rateRestaurant(
        @Path("orderId") orderId: String,
        @Body createRatingDto: CreateRatingDto
    ) : Response<Unit>
    @GET("users/ratings")
    suspend fun getRatings() : Response<List<RatingDto>>
    // Don't know if this is needed
    @GET("users/ratings/{id}")
    suspend fun getRating(
        @Path("id") orderId: String
    ) : Response<RatingDto>
    // Orders
    @GET("users/orders")
    suspend fun getOrders() : Response<List<Order>>
    @GET("users/orders/{id}")
    suspend fun getOrder(
        @Path("id") orderId: String
    ) : Response<Order>
    @DELETE("orders/{id}")
    suspend fun deleteOrder(
        @Path("id") orderId: String
    ) : Response<Unit>
}