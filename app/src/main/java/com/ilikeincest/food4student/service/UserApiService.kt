package com.ilikeincest.food4student.service

import com.ilikeincest.food4student.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApiService {
    @GET("lmao")
    suspend fun getLmao() : Response<String>
}