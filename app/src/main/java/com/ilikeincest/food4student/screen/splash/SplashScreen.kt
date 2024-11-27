package com.ilikeincest.food4student.screen.splash

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ilikeincest.food4student.util.nav.navigateAsRootRoute
import kotlinx.coroutines.delay
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.viewmodel.SignInViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import com.ilikeincest.food4student.AppRoutes

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    val accountService = signInViewModel.getAccountService()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
    }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        if (accountService.hasUser()) {
            val role = accountService.getUserRole()
            Log.d("SplashScreen", "User role: $role")
            if(role == "Admin"){
                navigateAsRootRoute(navController, AppRoutes.ADMIN.name)
            } else {
                navigateAsRootRoute(navController, AppRoutes.MAIN.name)
            }
        } else {
            navigateAsRootRoute(navController, AppRoutes.SIGN_IN.name)
        }
    }
}
