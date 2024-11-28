package com.ilikeincest.food4student.admin.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.admin.service.ModeratorApiService
import com.ilikeincest.food4student.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRestaurantViewModel @Inject constructor(
    private val apiService: ModeratorApiService
) : ViewModel() {

    // Stores the accumulated list of restaurants fetched from the server
    private var allRestaurants: MutableList<Restaurant> = mutableListOf()

    // The list of restaurants to display, filtered based on the search query and approval status
    var restaurants by mutableStateOf<List<Restaurant>>(emptyList())
        private set

    // The current search query
    var searchQuery by mutableStateOf("")
        private set

    // The currently selected filter status (true = show only approved, false = show all)
    var isFilterApproved by mutableStateOf(false)
        private set

    // Pagination variables
    private var currentPage = 1
    private val pageSize = 10
    var isLoading by mutableStateOf(false)
        private set
    var hasMore by mutableStateOf(true)
        private set

    init {
        // Initial fetch
        fetchRestaurants()
    }

    fun updateSearchQuery(newQuery: String) {
        searchQuery = newQuery
        filterRestaurants()
    }

    fun toggleFilterApproved() {
        isFilterApproved = !isFilterApproved
        filterRestaurants()
    }

    private fun filterRestaurants() {
        val query = searchQuery.lowercase().trim()
        restaurants = allRestaurants.filter { restaurant ->
            val matchesQuery = query.isEmpty() ||
                    restaurant.id.lowercase().contains(query) ||
                    restaurant.name.lowercase().contains(query)

            val matchesApproval = if (isFilterApproved) {
                restaurant.isApproved
            } else {
                true // Include all if not filtering
            }

            matchesQuery && matchesApproval
        }
    }

    fun fetchRestaurants() {
        // Prevent multiple simultaneous fetches and stop if no more data
        if (isLoading || !hasMore) return

        isLoading = true

        viewModelScope.launch {
            try {
                val response = apiService.getRestaurants(currentPage, pageSize)
                if (response.isSuccessful) {
                    val fetchedRestaurants = response.body() ?: emptyList()
                    if (fetchedRestaurants.size < pageSize) {
                        // Fewer items received than requested implies no more data
                        hasMore = false
                    }
                    // Append fetched restaurants to the accumulated list
                    allRestaurants.addAll(fetchedRestaurants)
                    filterRestaurants()
                    currentPage++ // Increment page for next fetch
                    Log.d("AdminRestaurantViewModel", "Fetched restaurants: $fetchedRestaurants")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AdminRestaurantViewModel", "Error fetching restaurants: $errorBody")
                    Log.e("AdminRestaurantViewModel", "HTTP status code: ${response.code()}")
                    Log.e("AdminRestaurantViewModel", "HTTP headers: ${response.headers()}")
                }
            } catch (e: Exception) {
                Log.e("AdminRestaurantViewModel", "Exception fetching restaurants", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun loadMoreRestaurants() {
        fetchRestaurants()
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
                        // Find and update the restaurant in the accumulated list
                        val index = allRestaurants.indexOfFirst { it.id == restaurantId }
                        if (index != -1) {
                            allRestaurants[index] = updatedRestaurant
                            filterRestaurants()
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