package com.ilikeincest.food4student.admin.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ilikeincest.food4student.admin.model.AdminRoutes
import com.ilikeincest.food4student.admin.screen.AdminRestaurantsScreen
import com.ilikeincest.food4student.admin.screen.AdminUsersScreen
import com.ilikeincest.food4student.admin.viewmodel.AdminRestaurantViewModel
import com.ilikeincest.food4student.admin.viewmodel.AdminUserViewModel
import com.ilikeincest.food4student.screen.account_center.AccountCenterScreen

@Composable
fun AdminNavGraph(navController: NavHostController, adminUserViewModel: AdminUserViewModel, adminRestaurantViewModel: AdminRestaurantViewModel) {
    NavHost(
        navController = navController,
        startDestination = AdminRoutes.Users.route
    ) {
        composable(AdminRoutes.Users.route) {
            AdminUsersScreen(adminUserViewModel)
        }
        composable(AdminRoutes.UnapprovedRestaurants.route) {
            AdminRestaurantsScreen(adminRestaurantViewModel)
        }
        composable(AdminRoutes.Account.route) {
            AccountCenterScreen(navController = navController)
        }
    }
}