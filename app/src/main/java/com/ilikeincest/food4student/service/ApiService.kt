package com.ilikeincest.food4student.service

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("lmao")
    suspend fun getLmao() : Response<String>
}