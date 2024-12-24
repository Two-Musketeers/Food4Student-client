package com.ilikeincest.food4student.screen.main_page.order

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.dto.NoNeedToFetchAgainBuddy
import com.ilikeincest.food4student.model.Order
import com.ilikeincest.food4student.model.OrderStatus
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.screen.main_page.home.dataStore
import com.ilikeincest.food4student.service.api.OrderApiService
import com.ilikeincest.food4student.service.api.RestaurantApiService
import com.ilikeincest.food4student.util.LocationUtils
import com.ilikeincest.food4student.util.haversineDistance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.Response
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "current_shipping")

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderApiService: OrderApiService,
    private val restaurantApiService: RestaurantApiService
): ViewModel() {
    val orderList = mutableStateMapOf<OrderStatus, List<Order>>()
    val error = mutableStateOf("")
    val isLoading = mutableStateOf(false)

    private suspend fun getOrdersOfStatus(it: OrderStatus): Response<List<Order>> {
        return when (it) {
            OrderStatus.Pending -> orderApiService.getOrdersPending()
            OrderStatus.Approved -> orderApiService.getOrdersApproved()
            OrderStatus.Delivered -> orderApiService.getOrdersDelivered()
            OrderStatus.Cancelled -> orderApiService.getOrdersCancelled()
        }
    }

    private var _isInitialized = false
    fun initData() {
        if (_isInitialized) return
        isLoading.value = true
        refreshOrders {
            isLoading.value = false
        }
        _isInitialized = true
    }
    fun refreshOrders(onSuccess: () -> Unit) {
        viewModelScope.launch {
            for (status in OrderStatus.entries) {
                val res = getOrdersOfStatus(status)
                if (!res.isSuccessful) {
                    error.value = "${res.code()} ${res.message()} - ${res.errorBody()!!.string()}"
                    return@launch
                }
                orderList[status] = res.body()!!
            }
            onSuccess()
        }
    }

    private val LOCATION = stringPreferencesKey("location")
    fun fetchRestaurant(id: String, context: Context, onSuccess: (NoNeedToFetchAgainBuddy) -> Unit) {
        viewModelScope.launch {
            val location = getCurrentLocation(context)
            val res = restaurantApiService.getRestaurantById(id)
            if (res.isSuccessful) {
                val body = res.body()!!
                val distance = haversineDistance(
                    lat1 = location?.latitude ?: body.latitude,
                    lon1 = location?.longitude ?: body.longitude,
                    lat2 = body.latitude,
                    lon2 = body.longitude
                )
                val estimatedTime = (distance / 40.0 * 60).toInt() // 40 km/h => time in minutes
                onSuccess(
                    NoNeedToFetchAgainBuddy(
                        Id = body.id,
                        Distance = distance,
                        TimeAway = estimatedTime,
                        IsFavorited = body.isFavorited
                    )
                )
            }
        }
    }

    private suspend fun getCurrentLocation(context: Context): SavedShippingLocation? {
        val data = context.dataStore.data.first()
        val str = data[LOCATION] ?: return null
        try {
            val location = Json.decodeFromString<SavedShippingLocation>(str)
            return location
        } catch (e: SerializationException) {
            context.dataStore.edit { it[LOCATION] = "" }
        }
        return null
    }
}