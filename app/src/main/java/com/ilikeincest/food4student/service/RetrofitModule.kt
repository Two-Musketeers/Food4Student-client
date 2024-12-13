package com.ilikeincest.food4student.service

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ilikeincest.food4student.BuildConfig
import com.ilikeincest.food4student.admin.service.ModeratorApiService
import com.ilikeincest.food4student.platform.retrofit.NetworkErrorInterceptor
import com.ilikeincest.food4student.service.api.*
import com.ilikeincest.food4student.util.InstantDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        account: AccountService,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val token = runBlocking { account.getUserToken() }
                val requestWithAuth = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestWithAuth)
            }
            .addInterceptor(NetworkErrorInterceptor(context))
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Instant::class.java, InstantDeserializer()) // Register the custom deserializer
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BACKEND_URL)
        .addConverterFactory(GsonConverterFactory.create(gson)) // Now 'gson' is provided as a parameter
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): UserApiService
        = retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideModeratorApiService(retrofit: Retrofit): ModeratorApiService
        = retrofit.create(ModeratorApiService::class.java)

    @Provides
    @Singleton
    fun provideAccountApiService(retrofit: Retrofit): AccountApiService
        = retrofit.create(AccountApiService::class.java)

    @Provides
    @Singleton
    fun provideOrderApiService(retrofit: Retrofit): OrderApiService
        = retrofit.create(OrderApiService::class.java)

    @Provides
    @Singleton
    fun provideRestaurantApiService(retrofit: Retrofit): RestaurantApiService
        = retrofit.create(RestaurantApiService::class.java)

    @Provides
    @Singleton
    fun provideVariationApiService(retrofit: Retrofit): VariationApiService
        = retrofit.create(VariationApiService::class.java)

    @Provides
    @Singleton
    fun providePhotoApiService(retrofit: Retrofit): PhotoApiService
        = retrofit.create(PhotoApiService::class.java)

    @Provides
    @Singleton
    fun provideFoodCategoryApiService(retrofit: Retrofit): FoodCategoryApiService
        = retrofit.create(FoodCategoryApiService::class.java)

    @Provides
    @Singleton
    fun provideFoodItemApiService(retrofit: Retrofit): FoodItemApiService
        = retrofit.create(FoodItemApiService::class.java)
}