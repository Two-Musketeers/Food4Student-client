package com.ilikeincest.food4student.screens.account_center

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.ilikeincest.food4student.MainActivity
import com.ilikeincest.food4student.model.User
import com.ilikeincest.food4student.model.service.AccountService
import com.ilikeincest.food4student.screens.ScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AccountCenterViewModel @Inject constructor(
    private val accountService: AccountService
) : ScreenViewModel() {
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

    fun onSignInClick(navController: NavController) {
        navController.navigate("SignInScreen")
    }

    fun onSignUpClick(navController: NavController) {
        navController.navigate("SignUpScreen")
    }

    fun onSignOutClick(navController: NavController, context: Context) {
        launchCatching {
            accountService.signOut()
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(context, intent, null)
        }
    }

    fun onDeleteAccountClick(navController: NavController, context: Context) {
        launchCatching {
            accountService.deleteAccount()
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(context, intent, null)
        }
    }
}