package com.ilikeincest.food4student.admin.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ilikeincest.food4student.admin.model.AdminRoutes

@Composable
fun AdminNavigationBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        val routes =
            listOf(AdminRoutes.Users, AdminRoutes.UnapprovedRestaurants, AdminRoutes.Account)
        routes.forEach { route ->
            NavigationBarItem(
                icon = { Icon(route.icon, contentDescription = null) },
                label = { Text(stringResource(route.labelResId)) },
                selected = currentRoute == route.route,
                onClick = {
                    if (currentRoute != route.route) {
                        navController.navigate(route.route) {
                            popUpTo(AdminRoutes.Users.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}