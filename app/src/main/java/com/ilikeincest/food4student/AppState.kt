package com.ilikeincest.food4student

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

@Stable
class AppState(val navController: NavHostController) {

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }
//    Might use in the future but for now none of the code needed them

//    fun popUp() {
//        navController.popBackStack()
//    }
//
//    fun navigate(route: String) {
//        navController.navigate(route) { launchSingleTop = true }
//    }
//
//    fun clearAndNavigate(route: String) {
//        navController.navigate(route) {
//            launchSingleTop = true
//            popUpTo(0) { inclusive = true }
//        }
//    }
}