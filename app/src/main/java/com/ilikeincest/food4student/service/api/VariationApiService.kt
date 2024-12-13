package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.*
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface VariationApiService {
    @GET("variations")
    suspend fun getVariations() : Response<List<VariationDto>>
    @POST("variations")
    suspend fun createVariation(
        variation: VariationCreateDto
    ) : Response<VariationDto>
    @PUT("variations/{id}")
    suspend fun updateVariation(
        variation: VariationCreateDto
    ) : Response<VariationDto>
    @DELETE("variations/{id}")
    suspend fun deleteVariation(
        id: String
    ) : Response<Unit>
    // Variation Options
    @POST("variations/{id}/variation-options")
    suspend fun createVariationOption(
        id: String,
        variationOption: VariationOptionCreateDto
    ) : Response<VariationOptionDto>
    @PUT("variations/{id}/variation-options/{optionId}")
    suspend fun updateVariationOption(
        id: String,
        optionId: String,
        variationOption: VariationOptionCreateDto
    ) : Response<VariationOptionDto>
    @DELETE("variations/{id}/variation-options/{optionId}")
    suspend fun deleteVariationOption(
        id: String,
        optionId: String
    ) : Response<Unit>
    // Food item variation
    @POST("variations/{id}/variation-options/{optionId}/food-items/{foodItemId}")
    suspend fun addFoodItemToVariationOption(
        id: String,
        optionId: String,
        foodItemId: String
    ) : Response<FoodItemVariationDto>
    @GET("variations/{id}/variation-options/{optionId}/food-items/{foodItemId}")
    suspend fun getFoodItemsFromVariationOption(
        id: String,
        optionId: String,
        foodItemId: String
    ) : Response<List<FoodItemVariationDto>>
    @DELETE("variations/{id}/variation-options/{optionId}/food-items/{foodItemId}")
    suspend fun removeFoodItemFromVariationOption(
        id: String,
        optionId: String,
        foodItemId: String
    ) : Response<Unit>
}