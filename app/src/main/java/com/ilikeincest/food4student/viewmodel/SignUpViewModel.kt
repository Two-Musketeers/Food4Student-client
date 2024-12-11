package com.ilikeincest.food4student.viewmodel

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.ilikeincest.food4student.AppRoutes
import com.ilikeincest.food4student.dto.DeviceTokenDto
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.service.api.AccountApiService
import com.ilikeincest.food4student.util.isValidEmail
import com.ilikeincest.food4student.util.isValidPassword
import com.ilikeincest.food4student.util.nav.navigateAsRootRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    private val accountApiService: AccountApiService
) : ViewModel() {
    // Backing properties to avoid state updates from other classes
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _emailError = MutableStateFlow("")
    val emailError: StateFlow<String> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow("")
    val passwordError: StateFlow<String> = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow("")
    val confirmPasswordError: StateFlow<String> = _confirmPasswordError.asStateFlow()

    fun updateEmail(newEmail: String) {
        _emailError.value = ""
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _passwordError.value = ""
        _password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        _confirmPasswordError.value = ""
        _confirmPassword.value = newConfirmPassword
    }

    fun onSignUpClick(navController: NavHostController) {
        launchCatching {
            var hasError = false
            if (!_email.value.isValidEmail()) {
                _emailError.value = "Invalid email format"
                hasError = true
            }

            if (!_password.value.isValidPassword()) {
                _passwordError.value = "Password must be at least 6 characters long and contain an uppercase letter"
                hasError = true
            }

            if (_password.value != _confirmPassword.value) {
                _confirmPasswordError.value = "Passwords do not match"
                hasError = true
            }

            if (hasError) return@launchCatching
            accountService.createAccountWithEmail(_email.value, _password.value)
            val token = Firebase.messaging.token.await()
            val deviceTokenDto = DeviceTokenDto(token)
            accountApiService.registerDeviceToken(deviceTokenDto)
            navigateAsRootRoute(navController, AppRoutes.MAIN.name)
        }
    }

    fun onGoogleSignIn(navController: NavHostController,credential: Credential) {
        launchCatching {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                accountService.signInWithGoogle(googleIdTokenCredential.idToken)
                val token = Firebase.messaging.token.await()
                val deviceTokenDto = DeviceTokenDto(token)
                accountApiService.registerDeviceToken(deviceTokenDto)
                navigateAsRootRoute(navController, AppRoutes.MAIN.name)
            } else {
                Log.e("SignInViewModel", "Unexpected credentials")
            }
        }
    }

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.d("SignInViewModel", throwable.message.orEmpty())
            }, block = block
        )
}