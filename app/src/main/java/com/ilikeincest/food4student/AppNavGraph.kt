package com.ilikeincest.food4student

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilikeincest.food4student.screen.main_page.MainScreen
import com.ilikeincest.food4student.screen.map.MapScreen

private enum class AppRoutes {
    MAIN,
    SIGN_IN,
    SIGN_UP,
    PROFILE,
    SHIPPING_LOCATION,
    PICK_LOCATION,
    MAP,
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.MAIN.name
    ) {
        composable(AppRoutes.MAIN.name) {
            MainScreen({ navController.navigate(AppRoutes.MAP.name) })
        }
        composable(AppRoutes.MAP.name) {
            MapScreen()
        }
    }
}