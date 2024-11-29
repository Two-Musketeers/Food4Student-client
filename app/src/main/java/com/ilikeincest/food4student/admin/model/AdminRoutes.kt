package com.ilikeincest.food4student.admin.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector
import com.ilikeincest.food4student.R

sealed class AdminRoutes(val route: String, val icon: ImageVector, @StringRes val labelResId: Int) {
    object Users : AdminRoutes("users", Icons.Default.Person, R.string.users)
    object UnapprovedRestaurants : AdminRoutes("unapproved_restaurants", Icons.Default.Restaurant, R.string.restaurants)
    object Account : AdminRoutes("account", Icons.Default.AccountCircle, R.string.account)
}