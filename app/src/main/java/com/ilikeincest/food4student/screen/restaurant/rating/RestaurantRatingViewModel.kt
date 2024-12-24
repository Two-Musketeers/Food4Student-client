package com.ilikeincest.food4student.screen.restaurant.rating

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.AppRoutes
import com.ilikeincest.food4student.dto.RatingDto
import com.ilikeincest.food4student.service.api.RestaurantApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantRatingViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val restaurantApiService: RestaurantApiService
): ViewModel() {
    private val _id: String? = handle[AppRoutes.RestaurantRating::id.name]
    private val _totalRatings = MutableStateFlow<Int>(0)
    private val _averageRating = MutableStateFlow<Double>(0.0)
    private val _perStarRatings = MutableStateFlow<List<Int>>(listOf())
    private val _ratings = MutableStateFlow<List<RatingDto>>(listOf())

    val totalRatings = _totalRatings.asStateFlow()
    val averageRating = _averageRating.asStateFlow()
    val perStarRatings = _perStarRatings.asStateFlow()
    val ratings = _ratings.asStateFlow()
    val errorMessage = mutableStateOf("")

    init {
        loadData()
    }

    private fun loadData() {
        if (_id == null) return
        viewModelScope.launch {
            val res1 = restaurantApiService.getRestaurantById(_id)
            if (!res1.isSuccessful) { with(res1) {
                errorMessage.value = "${code()} - ${errorBody()?.string()}"
                return@launch
            } }
            res1.body()?.let {
                _totalRatings.value = it.totalRatings
                _averageRating.value = it.averageRating
                _perStarRatings.value = listOf(5,5,5,5,5) // TODO
            }

            val res2 = restaurantApiService.restaurantRating(_id)

            if (!res2.isSuccessful) { with(res2) {
                errorMessage.value = "${code()} - ${errorBody()?.string()}"
                return@launch
            } }
            res2.body()?.let {
                _ratings.value = it
            }
        }
    }

    fun dismissError() {
        errorMessage.value = ""
    }
}