package com.ilikeincest.food4student.screen.auth.select_role

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.dto.RegisterAccountDto
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.service.api.AccountApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectRoleUserViewModel @Inject constructor(
    private val accountApi: AccountApiService,
    private val accountService: AccountService
): ViewModel() {
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _showLoading = MutableStateFlow(false)
    val showLoading = _showLoading.asStateFlow()

    fun registerUser(phoneNumber: String, onSuccess: () -> Unit) {
        // TODO: add validation
        _showLoading.value = true
        viewModelScope.launch {
            val res = accountApi.registerUser(RegisterAccountDto(phoneNumber))
            if (res.isSuccessful) {
                accountService.reloadToken()
                _showLoading.value = false
                onSuccess()
                return@launch
            }
            _showLoading.value = false
            _error.value = res.errorBody()?.string() ?: ""
        }
    }

    fun dismissError() {
        _error.value = ""
    }
}