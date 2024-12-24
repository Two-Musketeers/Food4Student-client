package com.ilikeincest.food4student.screen.auth.forget_password

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilikeincest.food4student.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val accountService: AccountService
): ViewModel() {
    val isLoading = mutableStateOf(false)
    val isSuccess = mutableStateOf(true)
    val dialogMessage = mutableStateOf("")

    fun forgetPassword(email: String) {
        launchCatching {
            accountService.forgetPassword(email)
            isSuccess.value = true
            dialogMessage.value = "Bạn kiểm tra mail nha!"
        }
    }
    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.d("SignInViewModel", throwable.message.orEmpty())
                isSuccess.value = false
                dialogMessage.value = throwable.message.orEmpty()
            }, block = block
        )
}