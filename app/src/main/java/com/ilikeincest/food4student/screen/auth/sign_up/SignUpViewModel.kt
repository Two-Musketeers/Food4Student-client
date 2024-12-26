package com.ilikeincest.food4student.screen.auth.sign_up

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.util.isValidEmail
import com.ilikeincest.food4student.util.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService
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

    fun setEmail(newEmail: String) {
        _emailError.value = ""
        _email.value = newEmail
    }

    fun setPassword(newPassword: String) {
        _passwordError.value = ""
        _password.value = newPassword
    }

    fun setConfirmPassword(newConfirmPassword: String) {
        _confirmPasswordError.value = ""
        _confirmPassword.value = newConfirmPassword
    }

    fun onSignUp(onSuccess: () -> Unit) {
        launchCatching {
            var hasError = false
            if (!_email.value.isValidEmail()) {
                _emailError.value = "Email không hợp lệ"
                hasError = true
            }

            if (!_password.value.isValidPassword()) {
                _passwordError.value = "Mật khẩu phải có từ 8 ký tự, và bao gồm cả chữ hoa, thường và số"
                hasError = true
            }

            if (_password.value != _confirmPassword.value) {
                _confirmPasswordError.value = "Mật khẩu không khớp"
                hasError = true
            }

            if (hasError) return@launchCatching
            accountService.createAccountWithEmail(_email.value, _password.value)
            onSuccess()
        }
    }

    fun onGoogleSignIn(credential: Credential, onSuccess: () -> Unit) {
        launchCatching {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                accountService.signInWithGoogle(googleIdTokenCredential.idToken)
                onSuccess()
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