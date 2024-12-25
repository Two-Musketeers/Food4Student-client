package com.ilikeincest.food4student.screen.account_center

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.ilikeincest.food4student.MainActivity
import com.ilikeincest.food4student.model.Account
import com.ilikeincest.food4student.service.AccountService
import com.ilikeincest.food4student.service.api.AccountApiService
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
class AccountCenterViewModel @Inject constructor(
    private val accountService: AccountService,
    private val accountApiService: AccountApiService
) : ViewModel() {
    // Backing property to avoid state updates from other classes
    private val _user = MutableStateFlow(Account())
    val user: StateFlow<Account> = _user.asStateFlow()

    init {
        launchCatching {
            _user.value = accountService.getUserProfile()
        }
    }

    fun onUpdateDisplayNameClick(newDisplayName: String) {
        launchCatching {
            accountService.updateDisplayName(newDisplayName)
            _user.value = accountService.getUserProfile()
        }
    }

    //To get the user session token and log it out in the LogCat
    fun logUserToken() {
        viewModelScope.launch {
            try {
                val token = accountService.getUserToken()
                Log.d("AccountCenterViewModel", "User token: $token")
            } catch (e: Exception) {
                Log.e("AccountCenterViewModel", "Error fetching user token", e)
            }
        }
    }

    fun onSignOutClick(context: Context) {
        launchCatching {
            val token = Firebase.messaging.token.await()
            accountApiService.deleteDeviceToken(token)
            accountService.signOut()
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent, null)
        }
    }


    fun onDeleteAccountClick(context: Context) {
        launchCatching {
            accountApiService.deleteUser()
            accountService.deleteAccount()
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent, null)
        }
    }

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.d("SignInViewModel", throwable.message.orEmpty())
            }, block = block
        )
}