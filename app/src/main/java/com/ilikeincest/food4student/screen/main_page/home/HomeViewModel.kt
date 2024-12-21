package com.ilikeincest.food4student.screen.main_page.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.model.Restaurant
import com.ilikeincest.food4student.service.api.RestaurantApiService
import com.ilikeincest.food4student.service.api.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val pageSize = 10

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantApi: RestaurantApiService,
    private val userApiService: UserApiService
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

    private val _selectedTab = MutableStateFlow(HomeTabTypes.Nearby)
    val selectedTab = _selectedTab.asStateFlow()

    private var _currentPage = 1

    fun selectTab(i: HomeTabTypes) { _selectedTab.value = i }

    suspend fun refreshRestaurantList() {
        _isRefreshing.value = true
        _isLoadingMore.value = false
        _currentPage = 1
        val res = restaurantApi.getRestaurants(1, pageSize)

        if (!res.isSuccessful) {
            _errorMessage.value = "Không thể load danh sách nhà hàng.\n" +
                    "Mã lỗi: ${res.code()}${res.message()}"
            return
        }

        restaurantList.clear()
        restaurantList.addAll(res.body() ?: listOf())
        _noMoreRestaurant.value = false
        _isRefreshing.value = false
    }

    fun loadMoreRestaurants() {
        // TODO: test this.
        _isLoadingMore.value = true
        _currentPage++
        viewModelScope.launch {
            val res = restaurantApi.getRestaurants(_currentPage, pageSize)

            if (!res.isSuccessful) {
                _errorMessage.value = "Không thể load danh sách nhà hàng.\n" +
                        "Mã lỗi: ${res.code()}${res.message()}"
                return@launch
            }

            val newList = res.body() ?: listOf()
            restaurantList.addAll(newList)

            if (newList.size < pageSize) {
                _noMoreRestaurant.value = true
            }

            _isLoadingMore.value = false
        }
    }

    fun toggleLike(restaurantId: String) {
        viewModelScope.launch {
            userApiService.toggleLikeRestaurant(restaurantId)
            val index = restaurantList.indexOfFirst { it.id == restaurantId }
            val newRes = restaurantList[index].copy(
                isLiked = !restaurantList[index].isLiked
            )
            restaurantList[index] = newRes
        }
    }

    fun dismissError() {
        _errorMessage.value = ""
    }
}