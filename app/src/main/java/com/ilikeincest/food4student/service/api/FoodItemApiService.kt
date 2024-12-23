package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.FoodItemDto
import com.ilikeincest.food4student.model.FoodItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FoodItemApiService {
    @POST("restaurants/food-categories/{foodCategoryId}/food-items/batch")
    suspend fun createFoodItem(
        @Body foodItem: FoodItem,
        @Path("foodCategoryId") foodCategoryId: String
    ) : Response<FoodItem>
    @DELETE("restaurants/food-categories/{foodCategoryId}/food-items/{id}")
    suspend fun deleteFoodItem(
        @Path("id") id: String,
        @Path("foodCategoryId") foodCategoryId: String
    ) : Response<Unit>
    @PUT("restaurants/food-categories/{foodCategoryId}/food-items/{foodItemId}")
    suspend fun updateFoodItem(
        @Path("foodItemId") foodItemId: String,
        @Path("foodCategoryId") foodCategoryId: String,
        @Body foodItem: FoodItem
    ) : Response<FoodItem>
}