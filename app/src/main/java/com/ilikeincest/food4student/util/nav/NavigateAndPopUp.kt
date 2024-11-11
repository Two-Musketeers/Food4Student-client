package com.ilikeincest.food4student.util.nav

import androidx.navigation.NavHostController

fun navigateAsRootRoute(navController: NavHostController, route: String) {
    navController.navigate(route) {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }
}