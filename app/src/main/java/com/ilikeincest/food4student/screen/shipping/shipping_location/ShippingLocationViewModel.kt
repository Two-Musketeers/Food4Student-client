package com.ilikeincest.food4student.screen.shipping.shipping_location

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.model.Location
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.model.SavedShippingLocationType
import com.ilikeincest.food4student.service.api.AccountApiService
import com.ilikeincest.food4student.service.api.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "current_shipping")

// address, lat lang,
@HiltViewModel
class ShippingLocationViewModel @Inject constructor(
    private val userApiService: UserApiService
): ViewModel() {
    // TODO: swap to non id model
    private val _currentLocation = MutableStateFlow(SavedShippingLocation(
        id = "",
        locationType = SavedShippingLocationType.Home,
        location = "",
        address = "",
        latitude = 0.0, longitude = 0.0,
    ))
    val currentLocation = _currentLocation.asStateFlow()

    val locationList = mutableStateListOf<SavedShippingLocation>()

    val error = mutableStateOf("")

    fun reloadLocationList() {
        viewModelScope.launch {
            val res = userApiService.getShippingAddresses()
            if (!res.isSuccessful) {
                error.value = "${res.code()} ${res.message()} - ${res.errorBody()}"
                return@launch
            }
            val body = res.body() ?: listOf()
            locationList.clear()
            locationList.addAll(body)
        }
    }

    private val LOCATION = stringPreferencesKey("location")
    fun fetchCurrentFromDStore(context: Context) {
        viewModelScope.launch {
            context.dataStore.data.map {
                val str = it[LOCATION] ?: return@map
                try {
                    val res = Json.decodeFromString<SavedShippingLocation>(str)
                    _currentLocation.value = res
                } catch (e: SerializationException) {
                    context.dataStore.edit { it[LOCATION] = "" }
                }
            }.stateIn(viewModelScope)
        }
    }

    fun setCurrent(location: SavedShippingLocation, context: Context) {
        viewModelScope.launch {
            context.dataStore.edit {
                it[LOCATION] = Json.encodeToString(location)
            }
        }
    }

    fun pickLocation(it: Location, context: Context) {
        setCurrent(SavedShippingLocation(
            id = "",
            locationType = SavedShippingLocationType.Home,
            location = it.location,
            address = it.address,
            latitude = it.latitude,
            longitude = it.longitude
        ), context)
    }
}