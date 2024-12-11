package com.ilikeincest.food4student.util.nav

import androidx.navigation.NavHostController

fun NavHostController.navigateAsRootRoute(route: Any) {
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }
}