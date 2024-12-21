package com.ilikeincest.food4student.screen.restaurant

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue

@Composable
fun RestaurantNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar {
        val items = listOf(
            MainRestaurantRoutes.Dashboard,
            MainRestaurantRoutes.Orders,
            MainRestaurantRoutes.Notifications,
            MainRestaurantRoutes.Account
        )
        items.forEach { route ->
            val isSelected = (currentRoute == route.route)
            NavigationBarItem(
                selected = isSelected,
                onClick = { if (!isSelected) navController.navigate(route.route) },
                icon = {
                    when (route) {
                        MainRestaurantRoutes.Dashboard -> Icon(Icons.Default.Home, contentDescription = route.label)
                        MainRestaurantRoutes.Orders -> Icon(Icons.AutoMirrored.Filled.List, contentDescription = route.label)
                        MainRestaurantRoutes.Notifications -> Icon(Icons.Default.Notifications, contentDescription = route.label)
                        MainRestaurantRoutes.Account -> Icon(Icons.Default.Person, contentDescription = route.label)
                        RestaurantRoutes.AddCategory -> TODO()
                        RestaurantRoutes.AddEditFoodItem -> TODO()
                        RestaurantRoutes.AddEditVariation -> TODO()
                    }
                },
                label = { Text(route.route) }
            )
        }
    }
}