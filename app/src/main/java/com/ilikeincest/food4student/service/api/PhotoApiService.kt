package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.dto.Photo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoApiService {
    @Multipart
    @POST("restaurants/photos/logo")
    suspend fun uploadLogo(
        @Part file: MultipartBody.Part
    ): Response<Photo>
    @Multipart
    @POST("restaurants/photos/banner")
    suspend fun uploadBanner(
        @Part file: MultipartBody.Part
    ): Response<Photo>
    @Multipart
    @POST("restaurants/photos/food-items/{foodItemId}")
    suspend fun uploadFoodItem(
        @Part file: MultipartBody.Part,
        @Path("foodItemId") foodItemId: String
    ): Response<Photo>
    @DELETE("restaurants/photos/logo")
    suspend fun deleteLogo(): Response<Unit>
    @DELETE("restaurants/photos/banner")
    suspend fun deleteBanner(): Response<Unit>
    @DELETE("restaurants/photos/food-items/{foodItemId}")
    suspend fun deleteFoodItem(
        @Path("foodItemId") foodItemId: String
    ): Response<Unit>
}