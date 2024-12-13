package com.ilikeincest.food4student.screen.splash

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.viewmodel.SignInViewModel
import kotlinx.coroutines.delay

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
    onSetRootAdmin: () -> Unit,
    onSetRootMain: () -> Unit,
    onSetRootSignIn: () -> Unit,
    vm: SplashScreenViewModel = hiltViewModel()
) {
    // setup toasts
    val toastMessage by vm.toastMessage.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(toastMessage) {
        if (toastMessage.isBlank()) return@LaunchedEffect
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        vm.clearToast() // toast is run by system, not by current LaunchedEffect coroutine
        // so wont be cancelled
    }
    // register device token to fcm
    LaunchedEffect(Unit) {
        vm.registerNotificationDeviceToken()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = colorScheme.onBackground)
    }

    LaunchedEffect(Unit) {
        if (vm.isSignedIn()) {
            val role = vm.getAccountRole()
            if (role == "Admin" || role == "Moderator") {
                onSetRootAdmin()
            } else {
                onSetRootMain()
            }
        } else {
            onSetRootSignIn()
        }
    }
}
