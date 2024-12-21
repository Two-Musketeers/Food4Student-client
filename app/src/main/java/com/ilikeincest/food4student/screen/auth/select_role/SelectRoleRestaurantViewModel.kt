package com.ilikeincest.food4student.screen.auth.select_role

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.dto.RegisterRestaurantOwnerDto
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.service.api.AccountApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectRoleRestaurantViewModel @Inject constructor(
    private val accountApi: AccountApiService,
    private val accountService: AccountService
): ViewModel() {
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _showLoading = MutableStateFlow(false)
    val showLoading = _showLoading.asStateFlow()

    // TODO: move to stateflow, but this is fine for now
    var name = mutableStateOf("")
    var description = mutableStateOf("")
    var phoneNumber = mutableStateOf("")
    var ownerPhoneNumber = mutableStateOf("")
    var address = mutableStateOf("")
    var latitude = mutableDoubleStateOf(Double.POSITIVE_INFINITY)
    var longitude = mutableDoubleStateOf(Double.POSITIVE_INFINITY)

    fun registerRestaurant(onSuccess: () -> Unit) {
        // TODO: add validation
        if (
            name.value.isBlank() ||
            phoneNumber.value.isBlank() ||
            ownerPhoneNumber.value.isBlank() ||
            address.value.isBlank()
        ) {
            _error.value = "Không được để trống thông tin (trừ mô tả)"
            return
        }
        _showLoading.value = true
        viewModelScope.launch {
            val res = accountApi.registerRestaurantOwner(RegisterRestaurantOwnerDto(
                name = name.value,
                description = description.value.ifBlank { null },
                address = address.value,
                phoneNumber = phoneNumber.value,
                ownerPhoneNumber = ownerPhoneNumber.value,
                latitude = latitude.doubleValue,
                longitude = longitude.doubleValue
            ))
            _showLoading.value = false
            if (res.isSuccessful) {
                accountService.reloadToken()
                onSuccess()
                return@launch
            }
            val error = res.errorBody()?.string() ?: ""
            _error.value = "${res.code()}: ${res.message()}\n$error"
        }
    }

    fun dismissError() {
        _error.value = ""
    }
}