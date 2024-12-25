package com.ilikeincest.food4student.screen.checkout.confirm

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.dto.VariationSelectionDto
import com.ilikeincest.food4student.dto.order.CreateOrderDto
import com.ilikeincest.food4student.dto.order.CreateOrderItemDto
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.screen.restaurant.detail.Cart
import com.ilikeincest.food4student.screen.shipping.shipping_location.dataStore
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.service.api.OrderApiService
import com.ilikeincest.food4student.service.api.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class CheckoutConfirmViewModel @Inject constructor(
    private val orderApiService: OrderApiService,
    private val accountService: AccountService,
    private val userApiService: UserApiService
): ViewModel() {
    private var userName: String? = null
    private var userPhone: String? = null
    private val _shippingLocation = MutableStateFlow<SavedShippingLocation?>(null)
    val shippingLocation = _shippingLocation.asStateFlow()

    val isLoading = mutableStateOf(false)
    val error = mutableStateOf("")

    private val LOCATION = stringPreferencesKey("location")
    fun initUserInfo(context: Context) {
        isLoading.value = true
        viewModelScope.launch {
            if (userName == null) {
                userName = accountService.getUserProfile().displayName
            }
            if (userPhone == null) {
                val res = userApiService.getPhoneNumber()
                if (!res.isSuccessful) {
                    isLoading.value = false
                    Toast.makeText(context, "Không thể lấy thông tin từ server", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                userPhone = res.body()!!.string() // shouldnt be null but eh
            }
            isLoading.value = false
        }
    }
    fun registerLocationFromDStore(context: Context) {
        viewModelScope.launch {
            context.dataStore.data.map {
                val str = it[LOCATION] ?: return@map
                try {
                    val res = Json.decodeFromString<SavedShippingLocation>(str)
                    _shippingLocation.value = res
                    if (_shippingLocation.value!!.phoneNumber == null) {
                        _shippingLocation.value = _shippingLocation.value!!.copy(
                            phoneNumber = userPhone
                        )
                    }
                    if (_shippingLocation.value!!.name == null) {
                        _shippingLocation.value = _shippingLocation.value!!.copy(
                            name = userName
                        )
                    }
                } catch (e: SerializationException) {
                    context.dataStore.edit { it[LOCATION] = "" }
                }
            }.stateIn(viewModelScope)
        }
    }

    fun commitOrder(order: Cart, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val orderItems = order.cartItems.map {
                val variations = it.selectedVariations.map {
                    VariationSelectionDto(
                        variationId = it.key,
                        variationOptionIds = it.value
                    )
                }
                CreateOrderItemDto(
                    foodItemId = it.foodItem.id,
                    quantity = it.quantity,
                    selectedVariations = variations
                )
            }
            val selectedLocation = _shippingLocation.value!!
            var shippingAddress = selectedLocation.address
            if (selectedLocation.location != selectedLocation.address
                && selectedLocation.location.isNotBlank()) {
                shippingAddress = "${selectedLocation.location}, $shippingAddress"
            }
            val res = orderApiService.createOrder(
                order.restaurantId,
                CreateOrderDto(
                    orderItems = orderItems,
                    shippingAddress = shippingAddress,
                    latitude = selectedLocation.latitude,
                    longitude = selectedLocation.longitude,
                    phoneNumber = selectedLocation.phoneNumber!!,
                    name = selectedLocation.name!!,
                    note = selectedLocation.buildingNote
                )
            )
            isLoading.value = false
            if (res.isSuccessful) {
                onSuccess()
            } else {
                error.value = res.errorBody()!!.string()
            }
        }
    }
}