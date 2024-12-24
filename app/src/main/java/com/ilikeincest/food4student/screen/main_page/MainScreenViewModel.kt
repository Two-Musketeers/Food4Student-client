package com.ilikeincest.food4student.screen.main_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.here.sdk.core.GeoCoordinates
import com.ilikeincest.food4student.model.Account
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.util.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val repository: RestaurantRepository
) : ViewModel() {
    private val _profile = MutableStateFlow(Account())
    val profile = _profile.asStateFlow()

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
}