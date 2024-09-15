package com.ilikeincest.food4student

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilikeincest.food4student.screens.account_center.AccountCenterScreen
import com.ilikeincest.food4student.screens.sign_in.SignInScreen
import com.ilikeincest.food4student.screens.sign_up.SignUpScreen
import com.ilikeincest.food4student.screens.splash.SplashScreen
import com.ilikeincest.food4student.ui.theme.Food4StudentTheme

@Composable
fun AppRoute() {
    Food4StudentTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()

            Scaffold { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = "SplashScreen",
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    notesGraph(appState)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        AppState(navController)
    }

fun NavGraphBuilder.notesGraph(appState: AppState) {
    composable("AccountCenterScreen") {
        AccountCenterScreen(navController = appState.navController)
    }
    composable("SignInScreen") {
        SignInScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable("SignUpScreen") {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable("SplashScreen") {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

}