package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.*
import com.ilikeincest.food4student.model.Variation
import com.ilikeincest.food4student.model.VariationOption
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VariationApiService {
    @GET("variations")
    suspend fun getVariations() : Response<List<Variation>>
    @POST("restaurants/food-categories/{foodCategoryId}/food-items/{foodItemId}/variations")
    suspend fun createVariation(
        @Path("foodCategoryId") foodCategoryId: String,
        @Path("foodItemId") foodItemId: String,
        @Body variation: VariationCreateDto
    ) : Response<Variation>
    @PUT("restaurants/food-categories/{foodCategoryId}/food-items/{foodItemId}/variations/{id}")
    suspend fun updateVariation(
        @Path("id") id: String,
        @Path("foodCategoryId") foodCategoryId: String,
        @Path("foodItemId") foodItemId: String,
        @Body variation: VariationCreateDto
    ) : Response<Variation>
    @DELETE("restaurants/food-categories/{foodCategoryId}/food-items/{foodItemId}/variations/{id}")
    suspend fun deleteVariation(
        @Path("id") id: String,
        @Path("foodCategoryId") foodCategoryId: String,
        @Path("foodItemId") foodItemId: String
    ) : Response<Unit>
    // Variation Options
    @POST("restaurants/food-categories/{foodCategoryId}/food-items/{foodItemId}/variations/{variationId}/variation-options")
    suspend fun createVariationOption(
        @Path("id") id: String,
        @Path("foodCategoryId") foodCategoryId: String,
        @Path("foodItemId") foodItemId: String,
        @Path("variationId") variationId: String,
        @Body variationOption: VariationOptionCreateDto
    ) : Response<VariationOption>
    @PUT("restaurants/food-categories/{foodCategoryId}/food-items/{foodItemId}/variations/{variationId}/variation-options/{optionId}")
    suspend fun updateVariationOption(
        @Path("optionId") optionId: String,
        @Path("foodCategoryId") foodCategoryId: String,
        @Path("foodItemId") foodItemId: String,
        @Path("variationId") variationId: String,
        variationOption: VariationOptionCreateDto
    ) : Response<VariationOption>
    @DELETE("restaurants/food-categories/{foodCategoryId}/food-items/{foodItemId}/variations/{variationId}/variation-options/{optionId}")
    suspend fun deleteVariationOption(
        @Path("id") id: String,
        @Path("foodCategoryId") foodCategoryId: String,
        @Path("foodItemId") foodItemId: String,
        @Path("optionId") optionId: String
    ) : Response<Unit>
}