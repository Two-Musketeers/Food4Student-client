package com.ilikeincest.food4student.screen.restaurant_owner

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ilikeincest.food4student.model.Location
import com.ilikeincest.food4student.screen.main_page.RequestNotificationPermissionDialog
import com.ilikeincest.food4student.screen.main_page.notification.NotificationScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantOwnerScreen(
    selectedLocation: Location?,
    viewModel: RestaurantOwnerViewModel,
    onNavigateToAddEditFoodItem: () -> Unit,
    onNavigateToRating: (id: String) -> Unit,
    onNavigateToLocationPicker: () -> Unit,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermissionDialog()
    }

    val restaurantState = viewModel.restaurant.collectAsState()
    val restaurant = restaurantState.value

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var currentRoute by rememberSaveable { mutableStateOf(defaultRoute) }
    Scaffold(
        bottomBar = { NavigationBar {
            for (route in RestaurantOwnerRoutes.entries) {
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
        topBar = {
            if (currentRoute == RestaurantOwnerRoutes.Account || currentRoute == RestaurantOwnerRoutes.Home) return@Scaffold
            TopAppBar(
                title = {
                    Text(currentRoute.topBarTitle)
                },
                actions = {
                    if (currentRoute == RestaurantOwnerRoutes.Notifications) {
                        val notiVm: NotificationScreenViewModel = hiltViewModel()
                        IconButton(onClick = { notiVm.markAllAsRead() }) {
                            Icon(Icons.Filled.Checklist, "Mark all as read")
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets =
            if (currentRoute == RestaurantOwnerRoutes.Account) WindowInsets.navigationBars
            else ScaffoldDefaults.contentWindowInsets
    ) { innerPadding ->
        RestaurantOwnerPageNavGraph(
            innerPadding = innerPadding,
            currentRoute = currentRoute,
            scrollConnection = scrollBehavior.nestedScrollConnection,
            onNavigateToAddEditFoodItem = { onNavigateToAddEditFoodItem() },
            viewModel = viewModel,
            onNavigateToRating = {onNavigateToRating(restaurant!!.id)},
            onNavigateToLocationPicker = onNavigateToLocationPicker,
            location = selectedLocation
        )
    }
}