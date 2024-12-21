package com.ilikeincest.food4student.screen.restaurant

sealed class MainRestaurantRoutes(val route: String, val label: String) {
    object Dashboard : MainRestaurantRoutes("dashboard", "Dashboard")
    object Orders : MainRestaurantRoutes("orders", "Orders")
    object Notifications : MainRestaurantRoutes("notifications", "Notifications")
    object Account : MainRestaurantRoutes("account", "Account")
}