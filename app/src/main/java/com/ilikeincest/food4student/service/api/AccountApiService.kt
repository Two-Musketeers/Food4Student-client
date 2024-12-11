package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.DeviceTokenDto
import com.ilikeincest.food4student.dto.RegisterAccountDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AccountApiService {
    @POST("account/device-token")
    suspend fun registerDeviceToken(@Body token: DeviceTokenDto): Response<Unit>
    @DELETE("account/device-token")
    suspend fun deleteDeviceToken(
        @Query("token") token: String
    ): Response<Unit>
    @POST("account/user-register")
    suspend fun registerUser(@Body userDto: RegisterAccountDto): Response<Unit>
    @POST("account/restaurantOwner-register")
    suspend fun registerRestaurantOwner(@Body userDto: RegisterAccountDto): Response<Unit>
    @DELETE("account")
    suspend fun deleteUser(): Response<Unit>
    @PUT("account")
    suspend fun updateUser(@Body userDto: RegisterAccountDto): Response<Unit>
}