package com.ilikeincest.food4student.screens.sign_up

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ilikeincest.food4student.model.service.AccountService
import com.ilikeincest.food4student.screens.ScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService
) : ScreenViewModel() {
    // Backing properties to avoid state updates from other classes
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit, context: Context) {
        launchCatching {
            try {
                if (!_email.value.isValidEmail()) {
                    throw IllegalArgumentException("Invalid email format")
                }

                if (!_password.value.isValidPassword()) {
                    throw IllegalArgumentException("Invalid password format")
                }

                if (_password.value != _confirmPassword.value) {
                    throw IllegalArgumentException("Passwords do not match")
                }

                accountService.createAccountWithEmail(_email.value, _password.value)
                openAndPopUp("AccountCenterScreen", "SignUpScreen")
            } catch (e: IllegalArgumentException) {
                Log.e("SignUpError", e.message.orEmpty())
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("SignUpError", e.message.orEmpty())
                Toast.makeText(context, "An unexpected error occurred", Toast.LENGTH_LONG).show()
            }
        }
    }
}