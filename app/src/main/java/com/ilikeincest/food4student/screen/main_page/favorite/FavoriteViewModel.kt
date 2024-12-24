package com.ilikeincest.food4student.screen.main_page.favorite

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ilikeincest.food4student.model.Restaurant
import com.ilikeincest.food4student.service.api.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.here.sdk.core.GeoCoordinates
import com.ilikeincest.food4student.util.RestaurantRepository
import com.ilikeincest.food4student.util.haversineDistance

private const val pageSize = 10

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val userApiService: UserApiService,
    private val repository: RestaurantRepository
) : ViewModel() {
    // the list that's shown on screen
    val restaurantList = mutableStateListOf<Restaurant>()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _noMoreRestaurant = MutableStateFlow(false)
    val noMoreRestaurant = _noMoreRestaurant.asStateFlow()

    private var _currentPage = 1

    init {
        viewModelScope.launch {
            repository.initializeLikedRestaurants()
            refreshFavorites(latitude = 0.0, longitude = 0.0) // Replace with actual location
        }

        // Observe liked restaurants and update the list accordingly
        viewModelScope.launch {
            repository.likedRestaurantIds.collect { likedIds ->
                // This removal is appropriate for FavoriteViewModel
                restaurantList.removeAll { !likedIds.contains(it.id) }

                // Update existing restaurants by replacing them with updated copies
                restaurantList.forEachIndexed { index, restaurant ->
                    val updatedRestaurant = restaurant.copy(
                        isFavorited = likedIds.contains(restaurant.id)
                    )
                    restaurantList[index] = updatedRestaurant
                }
            }
        }
    }

    suspend fun refreshFavorites(latitude: Double, longitude: Double) {
        _isRefreshing.value = true
        _isLoadingMore.value = false
        _currentPage = 1
        val res = userApiService.getLikes(1, pageSize)
        if (!res.isSuccessful) {
            _errorMessage.value = "Không thể load danh sách nhà hàng.\nMã lỗi: ${res.code()}${res.message()}"
            _isRefreshing.value = false
            return
        }

        restaurantList.clear()
        res.body()?.forEach { dto ->
            val distance = haversineDistance(lat1 = latitude, lon1 = longitude, lat2 = dto.latitude, lon2 = dto.longitude)
            val estimatedTime = (distance / 40.0 * 60).toInt()

            restaurantList.add(
                Restaurant(
                    id = dto.id,
                    isApproved = dto.isApproved,
                    name = dto.name,
                    description = dto.description,
                    address = dto.address,
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    logoUrl = dto.logoUrl,
                    bannerUrl = dto.bannerUrl,
                    totalRatings = dto.totalRatings,
                    averageRating = dto.averageRating,
                    isFavorited = repository.likedRestaurantIds.value.contains(dto.id),
                    foodCategories = emptyList(),
                    distanceInKm = distance,
                    estimatedTimeInMinutes = estimatedTime,
                    perStarRating = listOf()
                )
            )
        }
        _noMoreRestaurant.value = false
        _isRefreshing.value = false
    }

    fun loadMoreFavorites(currentLocation: GeoCoordinates) {
        _isLoadingMore.value = true
        _currentPage++
        viewModelScope.launch {
            val res = userApiService.getLikes(_currentPage, pageSize)
            if (!res.isSuccessful) {
                _errorMessage.value = "Không thể load danh sách nhà hàng.\nMã lỗi: ${res.code()} ${res.message()}"
                _isLoadingMore.value = false
                return@launch
            }

            val newList = res.body()?.map { dto ->
                val distance = haversineDistance(lat1 = currentLocation.latitude, lon1 = currentLocation.longitude, lat2 = dto.latitude, lon2 = dto.longitude)
                val estimatedTime = (distance / 40.0 * 60).toInt()

                Restaurant(
                    id = dto.id,
                    isApproved = dto.isApproved,
                    name = dto.name,
                    description = dto.description,
                    address = dto.address,
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    logoUrl = dto.logoUrl,
                    bannerUrl = dto.bannerUrl,
                    totalRatings = dto.totalRatings,
                    averageRating = dto.averageRating,
                    isFavorited = repository.likedRestaurantIds.value.contains(dto.id),
                    foodCategories = emptyList(),
                    distanceInKm = distance,
                    estimatedTimeInMinutes = estimatedTime,
                    perStarRating = listOf()
                )
            } ?: listOf()

            restaurantList.addAll(newList)

            if (newList.size < pageSize) {
                _noMoreRestaurant.value = true
            }

            _isLoadingMore.value = false
        }
    }

    fun toggleLike(restaurantId: String) {
        viewModelScope.launch {
            repository.toggleLike(restaurantId)
        }
    }

    fun dismissError() {
        _errorMessage.value = ""
    }
}