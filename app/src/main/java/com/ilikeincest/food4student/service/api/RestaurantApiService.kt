package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.RatingDto
import com.ilikeincest.food4student.dto.RestaurantDetailDto
import com.ilikeincest.food4student.dto.RestaurantRegisterDto
import com.ilikeincest.food4student.model.Restaurant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface RestaurantApiService {
    @GET("restaurants")
    suspend fun getRestaurants(
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int
    ) : Response<List<Restaurant>>
    @GET("restaurants")
    suspend fun getRestaurantById(
        @Query("id") id: String
    ) : Response<RestaurantDetailDto>
    @POST("restaurants")
    suspend fun addRestaurant(
        @Body restaurant : RestaurantRegisterDto
    ) : Response<Restaurant>
    @PUT("restaurants")
    suspend fun updateRestaurant(
        @Body restaurant : RestaurantRegisterDto
    ) : Response<Restaurant>
    @DELETE("restaurants")
    suspend fun deleteRestaurant() : Response<Unit>
    // Rating
    @GET("restaurants/{id}/ratings")
    suspend fun restaurantRating(
        @Query("id") id: String
    ) : Response<List<RatingDto>>
    @GET("restaurants/owned")
    suspend fun getOwnedRestaurants() : Response<RestaurantDetailDto>
}