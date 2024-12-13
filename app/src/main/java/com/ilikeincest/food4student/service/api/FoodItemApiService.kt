package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.FoodItemDto
import com.ilikeincest.food4student.dto.FoodItemRegisterDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface FoodItemApiService {
    @POST("restaurants/food-items")
    suspend fun createFoodItem(
        @Body foodItem: FoodItemRegisterDto
    ) : Response<FoodItemDto>
    @DELETE("restaurants/food-items")
    suspend fun deleteFoodItem(
        @Query("id") id: String
    ) : Response<Unit>
    @PUT("restaurants/food-items")
    suspend fun updateFoodItem(
        @Query("id") id: String,
        @Body foodItem: FoodItemRegisterDto
    ) : Response<FoodItemDto>
}