package com.ilikeincest.food4student

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

private enum class AppRoutes {
    HOME,
    LOGIN,
    REGISTER,
    PROFILE,
    SETTINGS,
    SEARCH,
    ADD,
    EDIT,
    VIEW
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

    }
}