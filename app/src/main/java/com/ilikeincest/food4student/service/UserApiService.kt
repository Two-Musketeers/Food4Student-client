package com.ilikeincest.food4student.service

import com.ilikeincest.food4student.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApiService {
    @GET("lmao")
    suspend fun getLmao() : Response<String>
    @GET("moderator/users")
    suspend fun getUsers(
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int
    ) : Response<List<User>>
    @PUT("moderator/users")
    suspend fun updateUserRole(
        @Query("UserId") userId: String,
        @Query("Role") role: String
    ) : Response<User>
}