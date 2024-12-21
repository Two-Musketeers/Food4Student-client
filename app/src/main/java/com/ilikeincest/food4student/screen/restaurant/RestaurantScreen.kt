package com.ilikeincest.food4student.screen.restaurant

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ilikeincest.food4student.screen.main_page.RequestNotificationPermissionDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreen(
    viewModel: RestaurantViewModel,
    onNavigateToAddEditFoodItem: () -> Unit,
    navController: NavController,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermissionDialog()
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var currentRoute by rememberSaveable { mutableStateOf(defaultRoute) }
    Scaffold(
        bottomBar = { NavigationBar {
            for (route in MainRestaurantRoutes.entries) {
                val isSelected = route == currentRoute
                val iconRes = if (isSelected) route.selectedIcon
                    else route.unselectedIcon
                val icon = @Composable {
                    Icon(iconRes, null)
                }
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != route)
                            currentRoute = route
                    },
                    label = { Text(stringResource(route.labelResId)) },
                    icon = icon
                )
            }
        } },
    ) {
        Box {
            RestaurantPageNavGraph(
                currentRoute = currentRoute,
                scrollConnection = scrollBehavior.nestedScrollConnection,
                onNavigateToAddEditFoodItem = { onNavigateToAddEditFoodItem() },
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}