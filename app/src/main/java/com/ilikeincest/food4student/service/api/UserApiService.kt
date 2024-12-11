package com.ilikeincest.food4student.service.api

import com.ilikeincest.food4student.model.Notification
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("lmao")
    suspend fun getLmao() : Response<String>
    @GET("users/notifications")
    suspend fun getNotifications() : Response<List<Notification>>
}