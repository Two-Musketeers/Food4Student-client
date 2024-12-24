package com.ilikeincest.food4student.screen.main_page

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.here.sdk.core.GeoCoordinates
import com.ilikeincest.food4student.DEBOUNCE_DELAY
import com.ilikeincest.food4student.model.Account
import com.ilikeincest.food4student.model.Restaurant
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.service.api.RestaurantApiService
import com.ilikeincest.food4student.util.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val repository: RestaurantRepository,
    private val restaurantApiService: RestaurantApiService
) : ViewModel() {
    private val _profile = MutableStateFlow(Account())
    val profile = _profile.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Restaurant>>(emptyList())
    val searchResults: StateFlow<List<Restaurant>> = _searchResults

    val errorMessage = mutableStateOf("")

    private val _currentLocation = MutableStateFlow<GeoCoordinates?>(null)
    val currentLocation: StateFlow<GeoCoordinates?> = _currentLocation.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeLikedRestaurants()
        }
    }

    fun refreshUserProfile() {
        _profile.value = accountService.getUserProfile()
    }

    fun updateCurrentLocation(location: GeoCoordinates) {
        _currentLocation.value = location
    }

    fun searchRestaurants(query: String, pageNumber: Int = 1, pageSize: Int = 20) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            delay(DEBOUNCE_DELAY) // Debounce to avoid excessive calls
            try {
                val response = restaurantApiService.searchRestaurants(query, pageNumber, pageSize)
                if (response.isSuccessful) {
                    response.body()?.let { results ->
                        _searchResults.value = results
                    } ?: run {
                        errorMessage.value = "No results found."
                    }
                } else {
                    errorMessage.value = "Search Error ${response.code()}: ${response.errorBody()?.string() ?: "Unknown error"}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Exception during search: ${e.message}"
            }
        }
    }

    fun dismissError() {
        errorMessage.value = ""
    }
}