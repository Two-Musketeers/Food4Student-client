package com.ilikeincest.food4student.screen.restaurant

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ilikeincest.food4student.screen.account_center.AccountCenterScreen
import com.ilikeincest.food4student.screen.food_item.add_category.AddCategoryScreen
import com.ilikeincest.food4student.screen.food_item.add_edit_saved_product.AddEditSavedFoodItemScreen
import com.ilikeincest.food4student.screen.food_item.add_edit_saved_varations.AddEditSavedVariationScreen
import com.ilikeincest.food4student.screen.main_page.notification.NotificationScreen
import com.ilikeincest.food4student.screen.main_page.order.OrderScreen

@Composable
fun RestaurantNavGraph(
    navController: NavHostController,
    restaurantViewModel: RestaurantViewModel,
    nestedScrollConnection: NestedScrollConnection
) {
    NavHost(
        navController = navController,
        startDestination = MainRestaurantRoutes.Dashboard.route
    ) {
        composable(MainRestaurantRoutes.Dashboard.route) {
            // The “main” restaurant screen (list of dishes, etc.)
            RestaurantScreenContent(
                onNavigateToAddEditFoodItem = {
                    navController.navigate(RestaurantRoutes.AddEditFoodItem.route)
                },
                viewModel = restaurantViewModel
            )
        }
        composable(MainRestaurantRoutes.Orders.route) {
            // Example “Orders” screen
            OrderScreen()
        }
        composable(MainRestaurantRoutes.Notifications.route) {
            NotificationScreen(nestedScrollConnection)
        }
        composable(MainRestaurantRoutes.Account.route) {
            AccountCenterScreen(navController = navController)
        }
        composable(RestaurantRoutes.AddEditFoodItem.route) {
            AddEditSavedFoodItemScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToFoodCategory = { navController.navigate(RestaurantRoutes.AddCategory.route) },
                onNavigateToVariation = { navController.navigate(RestaurantRoutes.AddEditVariation.route) },
                viewModel = restaurantViewModel
            )
        }
        composable(RestaurantRoutes.AddCategory.route) {
            AddCategoryScreen(
                onNavigateUp = { navController.navigateUp() },
                viewModel = restaurantViewModel,
                onCategorySelected = { category ->
                    restaurantViewModel.selectFoodCategory(category)
                    navController.navigateUp()
                }
            )
        }
        composable(RestaurantRoutes.AddEditVariation.route) {
            AddEditSavedVariationScreen(
                onNavigateUp = { navController.navigateUp() },
                viewModel = restaurantViewModel
            )
        }
    }
}

sealed class RestaurantRoutes(val route: String, val label: String) {
    object AddEditFoodItem : MainRestaurantRoutes("add_edit_food_item", "Add/Edit Food Item")
    object AddCategory : MainRestaurantRoutes("add_category", "Add Category")
    object AddEditVariation : MainRestaurantRoutes("add_edit_variation", "Add/Edit Variation")
}