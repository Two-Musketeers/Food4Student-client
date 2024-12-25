package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.DeviceTokenDto
import com.ilikeincest.food4student.dto.RegisterAccountDto
import com.ilikeincest.food4student.dto.RegisterRestaurantOwnerDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApiService {
    @POST("account/device-token")
    suspend fun registerDeviceToken(@Body token: DeviceTokenDto): Response<Unit>
    @DELETE("account/device-token/{token}")
    suspend fun deleteDeviceToken(
        @Path("token") token: String
    ): Response<Unit>
    @POST("account/user-register")
    suspend fun registerUser(@Body userDto: RegisterAccountDto): Response<Unit>
    @POST("account/restaurantOwner-register")
    suspend fun registerRestaurantOwner(
        @Body restaurantOwnerDto: RegisterRestaurantOwnerDto
    ): Response<Unit>
    @DELETE("account")
    suspend fun deleteUser(): Response<Unit>
}