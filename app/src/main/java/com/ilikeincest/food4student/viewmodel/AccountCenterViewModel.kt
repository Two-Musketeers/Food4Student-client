package com.ilikeincest.food4student.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ilikeincest.food4student.MainActivity
import com.ilikeincest.food4student.model.User
import com.ilikeincest.food4student.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountCenterViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {
    // Backing property to avoid state updates from other classes
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()

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

    fun onSignOutClick(navController: NavController, context: Context) {
        launchCatching {
            accountService.signOut()
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent, null)
        }
    }

    fun onDeleteAccountClick(navController: NavController, context: Context) {
        launchCatching {
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