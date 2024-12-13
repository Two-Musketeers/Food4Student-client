package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.FoodCategoryCreateDto
import com.ilikeincest.food4student.dto.FoodCategoryDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodCategoryApiService {
    @POST("restaurants/food-categories")
    suspend fun addFoodCategory(@Body foodCategory: FoodCategoryCreateDto) : Response<FoodCategoryDto>
    @PUT("restaurants/food-categories")
    suspend fun updateFoodCategory(
        @Path("id") id: String,
        @Body foodCategory: FoodCategoryCreateDto
    ) : Response<FoodCategoryDto>
    @DELETE("restaurants/food-categories")
    suspend fun deleteFoodCategory(
        @Path("id") id: String
    ) : Response<Unit>
    @POST("restaurants/food-categories/{foodCategoryId}/migrate-food-item/{foodItemId}")
    suspend fun migrateFoodItem(
        @Path("foodCategoryId") foodCategoryId: String,
        @Path("foodItemId") foodItemId: String
    ) : Response<Unit>
}