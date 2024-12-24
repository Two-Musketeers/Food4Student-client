package com.ilikeincest.food4student.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.ilikeincest.food4student.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    var error = mutableStateOf("")

    fun setEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun setPassword(newPassword: String) {
        _password.value = newPassword
    }

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.d("SignInViewModel", throwable.message.orEmpty())
                error.value = throwable.message.orEmpty()
            }, block = block
        )

    fun onSignIn(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // TODO: fucking handle wrong creds, wtf
            try {
                accountService.signInWithEmail(_email.value, _password.value)
                onSuccess()
            } catch (_: IllegalArgumentException) {
                error.value = "Vui lòng nhập đủ thông tin!"
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                error.value = "Thông tin không đúng, bạn kiếm tra lại nha (*/ω＼*)"
            }
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
}