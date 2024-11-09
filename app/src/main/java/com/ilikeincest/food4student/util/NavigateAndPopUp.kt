package com.ilikeincest.food4student.util

import androidx.navigation.NavHostController

fun navigateAndPopUp(navController: NavHostController, route: String, popUp: String) {
    navController.navigate(route) {
        launchSingleTop = true
        popUpTo(popUp) { inclusive = true }
    }
}