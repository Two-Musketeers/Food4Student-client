package com.ilikeincest.food4student.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.ilikeincest.food4student.dto.DeviceTokenDto
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.service.api.AccountApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
    private val accountApiService: AccountApiService,
    private val accountService: AccountService
) : ViewModel() {
    private val _toastMessage = MutableStateFlow("")
    val toastMessage = _toastMessage.asStateFlow()

    fun registerNotificationDeviceToken() {
        viewModelScope.launch {
            try {
                val token = Firebase.messaging.token.await()
                accountApiService.registerDeviceToken(DeviceTokenDto(token))
            } catch (e: Exception) {
                _toastMessage.value = "Không thể bật thông báo, vui lòng khởi động lại app."
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = ""
    }

    fun isSignedIn() = accountService.hasUser()
    suspend fun getAccountRole() = accountService.getUserRole()
}