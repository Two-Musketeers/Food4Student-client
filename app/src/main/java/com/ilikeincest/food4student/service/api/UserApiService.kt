package com.ilikeincest.food4student.service.api

import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("lmao")
    suspend fun getLmao() : Response<String>
}