package com.ilikeincest.food4student.admin.service

import com.ilikeincest.food4student.model.Restaurant
import com.ilikeincest.food4student.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ModeratorApiService {
    @GET("moderator/restaurants")
    suspend fun getRestaurants(
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int
    ): Response<List<Restaurant>>

    @GET("moderator/users")
    suspend fun getUsers(
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int
    ): Response<List<User>>

    @PUT("moderator/users")
    suspend fun updateUserRole(
        @Query("UserId") userId: String,
        @Query("Role") role: String
    ): Response<User>

    // Separate endpoints for approving and unapproving restaurants
    @PUT("moderator/restaurants/approve/{id}")
    suspend fun approveRestaurant(
        @Path("id") restaurantId: String
    ): Response<Restaurant>

    @PUT("moderator/restaurants/unApprove/{id}")
    suspend fun unApproveRestaurant(
        @Path("id") restaurantId: String
    ): Response<Restaurant>
}