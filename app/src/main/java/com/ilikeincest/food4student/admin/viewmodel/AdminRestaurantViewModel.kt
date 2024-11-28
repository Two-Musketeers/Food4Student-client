package com.ilikeincest.food4student.admin.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.admin.service.ModeratorApiService
import com.ilikeincest.food4student.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State

@HiltViewModel
class AdminRestaurantViewModel @Inject constructor(
    private val apiService: ModeratorApiService
) : ViewModel() {

    // Stores the full list of restaurants fetched from the server
    private var allRestaurants: List<Restaurant> = emptyList()

    // The list of restaurants to display, filtered based on the search query and approval status
    private val _restaurants = mutableStateOf<List<Restaurant>>(emptyList())
    val restaurants: State<List<Restaurant>> get() = _restaurants

    // The current search query
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> get() = _searchQuery

    // The current filter status (true = show only approved, false = show all)
    private val _isFilterApproved = mutableStateOf(false)
    val isFilterApproved: State<Boolean> get() = _isFilterApproved

    init {
        fetchRestaurants(pageNumber = 1, pageSize = 30)
    }

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
        filterRestaurants()
    }

    fun toggleFilterApproved() {
        _isFilterApproved.value = !_isFilterApproved.value
        filterRestaurants()
    }

    private fun filterRestaurants() {
        val query = _searchQuery.value.lowercase().trim()
        _restaurants.value = allRestaurants.filter { restaurant ->
            val matchesQuery = query.isEmpty() ||
                    restaurant.id.lowercase().contains(query) ||
                    restaurant.name.lowercase().contains(query)

            val matchesApproval = if (_isFilterApproved.value) {
                restaurant.isApproved
            } else {
                true // Include all if not filtering
            }

            matchesQuery && matchesApproval
        }
    }

    fun fetchRestaurants(pageNumber: Int, pageSize: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getRestaurants(pageNumber, pageSize)
                if (response.isSuccessful) {
                    allRestaurants = response.body() ?: emptyList()
                    filterRestaurants()
                    Log.d("AdminRestaurantViewModel", "Fetched restaurants: $allRestaurants")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AdminRestaurantViewModel", "Error fetching restaurants: $errorBody")
                    Log.e("AdminRestaurantViewModel", "HTTP status code: ${response.code()}")
                    Log.e("AdminRestaurantViewModel", "HTTP headers: ${response.headers()}")
                }
            } catch (e: Exception) {
                Log.e("AdminRestaurantViewModel", "Exception fetching restaurants", e)
            }
        }
    }

    fun updateRestaurantApproval(restaurantId: String, shouldApprove: Boolean) {
        viewModelScope.launch {
            try {
                val response = if (shouldApprove) {
                    apiService.approveRestaurant(restaurantId)
                } else {
                    apiService.unApproveRestaurant(restaurantId)
                }
                if (response.isSuccessful) {
                    val updatedRestaurant = response.body()
                    if (updatedRestaurant != null) {
                        val index = allRestaurants.indexOfFirst { it.id == restaurantId }
                        if (index != -1) {
                            allRestaurants = allRestaurants.toMutableList().apply {
                                set(index, updatedRestaurant)
                            }
                            filterRestaurants()
                            Log.d("AdminRestaurantViewModel", "Updated restaurant: $updatedRestaurant")
                        }
                    }
                } else {
                    Log.e(
                        "AdminRestaurantViewModel",
                        "Error updating restaurant approval: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("AdminRestaurantViewModel", "Exception updating restaurant approval", e)
            }
        }
    }
}