package com.ilikeincest.food4student.util

import com.ilikeincest.food4student.service.api.UserApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

const val pageSize = 10

@Singleton
class RestaurantRepository @Inject constructor(
    private val userApiService: UserApiService
) {
    // Holds the set of liked restaurant IDs
    private val _likedRestaurantIds = MutableStateFlow<Set<String>>(emptySet())
    val likedRestaurantIds: StateFlow<Set<String>> = _likedRestaurantIds.asStateFlow()

    // Initialize by loading liked restaurants from the server
    suspend fun initializeLikedRestaurants() {
        val res = userApiService.getLikes(1, pageSize)
        if (res.isSuccessful) {
            val likes = res.body()?.map { it.id }?.toSet() ?: emptySet()
            _likedRestaurantIds.value = likes
        } else {
            // Handle error appropriately (e.g., log or propagate)
        }
    }

    // Toggle like status and update the state
    suspend fun toggleLike(restaurantId: String) {
        val res = userApiService.toggleLikeRestaurant(restaurantId)
        if (res.isSuccessful) {
            _likedRestaurantIds.value = _likedRestaurantIds.value.toMutableSet().apply {
                if (contains(restaurantId)) {
                    remove(restaurantId)
                } else {
                    add(restaurantId)
                }
            }
        } else {
            // Handle error (e.g., show a message to the user)
        }
    }
}