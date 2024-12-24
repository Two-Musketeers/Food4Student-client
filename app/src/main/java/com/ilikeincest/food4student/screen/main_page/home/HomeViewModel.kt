package com.ilikeincest.food4student.screen.main_page.home

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.here.sdk.core.GeoCoordinates
import com.ilikeincest.food4student.model.Restaurant
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.screen.shipping.shipping_location.dataStore
import com.ilikeincest.food4student.service.api.RestaurantApiService
import com.ilikeincest.food4student.service.api.UserApiService
import com.ilikeincest.food4student.util.RestaurantRepository
import com.ilikeincest.food4student.util.haversineDistance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val pageSize = 10

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "current_shipping")

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantApi: RestaurantApiService,
    private val userApiService: UserApiService,
    private val repository: RestaurantRepository
) : ViewModel() {
    private val _shippingLocation = MutableStateFlow("")
    val shippingLocation = _shippingLocation.asStateFlow()

    private val LOCATION = stringPreferencesKey("location")
    fun fetchCurrentFromDStore(context: Context) {
        viewModelScope.launch {
            context.dataStore.data.map {
                val str = it[LOCATION] ?: return@map
                try {
                    val res = Json.decodeFromString<SavedShippingLocation>(str)
                    _shippingLocation.value = res.location
                } catch (e: SerializationException) {
                    context.dataStore.edit { it[LOCATION] = "" }
                }
            }.stateIn(viewModelScope)
        }
    }

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

    private val _selectedTab = MutableStateFlow(HomeTabTypes.Nearby)
    val selectedTab = _selectedTab.asStateFlow()

    private var _currentPage = 1

    init {
        viewModelScope.launch {
            repository.initializeLikedRestaurants()
        }

        // Observe liked restaurants and update the list accordingly
        viewModelScope.launch {
            repository.likedRestaurantIds.collect { likedIds ->
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

    fun selectTab(i: HomeTabTypes) { _selectedTab.value = i }

    suspend fun refreshRestaurantList(latitude: Double, longitude: Double) {
        _isRefreshing.value = true
        _isLoadingMore.value = false
        _currentPage = 1
        val res = restaurantApi.getRestaurants(latitude, longitude, 1, pageSize)
        if (!res.isSuccessful) {
            _errorMessage.value = "Không thể load danh sách nhà hàng.\n" +
                    "Mã lỗi: ${res.code()}${res.message()}"
            _isRefreshing.value = false
            return
        }

        restaurantList.clear()
        res.body()?.forEach { dto ->
            val distance = haversineDistance(
                lat1 = latitude,
                lon1 = longitude,
                lat2 = dto.latitude,
                lon2 = dto.longitude
            )
            val estimatedTime = (distance / 40.0 * 60).toInt() // 40 km/h => time in minutes

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
                    perStarRating = listOf() // TODO
                )
            )
        }
        _noMoreRestaurant.value = false
        _isRefreshing.value = false
    }

    fun loadMoreRestaurants() {
        _isLoadingMore.value = true
        _currentPage++
        viewModelScope.launch {
            while (_currentLocation.value == null) {
                delay(1000)
            }
            val currentLocation = _currentLocation.value!!
            val res = restaurantApi.getRestaurants(currentLocation.latitude, currentLocation.longitude, _currentPage, pageSize)

            if (!res.isSuccessful) {
                _errorMessage.value = "Không thể load danh sách nhà hàng.\n" +
                        "Mã lỗi: ${res.code()} ${res.message()}"
                _isLoadingMore.value = false
                return@launch
            }

            val newList = res.body()?.map { dto ->
                val distance = haversineDistance(
                    lat1 = currentLocation.latitude,
                    lon1 = currentLocation.longitude,
                    lat2 = dto.latitude,
                    lon2 = dto.longitude
                )
                val estimatedTime = (distance / 40.0 * 60).toInt() // 40 km/h => time in minutes

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