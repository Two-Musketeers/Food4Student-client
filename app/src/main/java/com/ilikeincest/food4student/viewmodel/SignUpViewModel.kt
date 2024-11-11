package com.ilikeincest.food4student.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.ilikeincest.food4student.AppRoutes
import com.ilikeincest.food4student.model.service.AccountService
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

    private val _errorText = MutableStateFlow<String?>(null)
    val errorText: StateFlow<String?> = _errorText.asStateFlow()

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    fun onSignUpClick(navController: NavHostController) {
        launchCatching {
            try {
                _errorText.value = null

                if (!_email.value.isValidEmail()) {
                    _errorText.value = "Invalid email format"
                    throw IllegalArgumentException("Invalid email format")
                }

                if (!_password.value.isValidPassword()) {
                    _errorText.value = "Password must be at least 6 characters long and contain an uppercase letter"
                    throw IllegalArgumentException("Invalid password format")
                }

                if (_password.value != _confirmPassword.value) {
                    _errorText.value = "Passwords do not match"
                    throw IllegalArgumentException("Passwords do not match")
                }

                accountService.createAccountWithEmail(_email.value, _password.value)
                navigateAndPopUp(navController, AppRoutes.MAIN.name, AppRoutes.SIGN_UP.name)
            } catch (e: IllegalArgumentException) {
                Log.e("SignUpError", e.message.orEmpty())
            } catch (e: Exception) {
                Log.e("SignUpError", e.message.orEmpty())
            }
        }
    }

    fun onGoogleSignIn(navController: NavHostController,credential: Credential) {
        launchCatching {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                accountService.signInWithGoogle(googleIdTokenCredential.idToken)
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