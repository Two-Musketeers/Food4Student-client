package com.ilikeincest.food4student.screen.restaurant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreen(
    restaurantViewModel: RestaurantViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBottomBar = when (currentRoute) {
        MainRestaurantRoutes.Dashboard.route,
        MainRestaurantRoutes.Orders.route,
        MainRestaurantRoutes.Notifications.route,
        MainRestaurantRoutes.Account.route -> true
        else -> false
    }
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                RestaurantNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            RestaurantNavGraph(navController, restaurantViewModel, scrollBehavior.nestedScrollConnection)
        }
    }
}