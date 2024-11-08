package com.ilikeincest.food4student.screen.main_page

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ilikeincest.food4student.R

private enum class MainRoutes(
    @StringRes val labelResId: Int,
    val unselectedIcon: @Composable () -> Unit,
    val selectedIcon: @Composable () -> Unit
) {
    HOME(
        R.string.home_screen_label,
        { Icon(Icons.Outlined.Fastfood, null) },
        { Icon(Icons.Filled.Fastfood, null) }
    ),
    ORDER(
        R.string.order_screen_label,
        { Icon(Icons.AutoMirrored.Outlined.ReceiptLong, null) },
        { Icon(Icons.AutoMirrored.Filled.ReceiptLong, null) }
    ),
    FAVORITE(
        R.string.favorite_screen_label,
        // This is utterly stupid. Fuck google. And their stupid ass everything.
        // Why the fuck is Icons.Outlined.Bookmark the same as Icons.Filled.Bookmark???
        // Then what the fuck does Outlined even mean???
        { Icon(Icons.Outlined.BookmarkBorder, null) },
        { Icon(Icons.Filled.Bookmark, null) },
    ),
    NOTIFICATION(
        R.string.notification_screen_label,
        { Icon(Icons.Outlined.Notifications, null) },
        { Icon(Icons.Filled.Notifications, null) }
    ),
}

@Composable
fun MainScreen(
    onNavigateToMap: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.let { route ->
        MainRoutes.entries.find { it.name == route }
    } ?: MainRoutes.HOME

    // TODO: add logic to populate this on load and maybe move to viewmodel
    val badgeInNavBar = remember { mutableStateMapOf<MainRoutes, String>(
        Pair(MainRoutes.ORDER, "5"),
        Pair(MainRoutes.NOTIFICATION, "3")
    ) }

    Scaffold(
        bottomBar = { NavigationBar {
            for (route in MainRoutes.entries) {
                val isSelected = route == currentRoute
                val icon = if (isSelected) route.selectedIcon
                    else route.unselectedIcon
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { navController.navigate(route.name) },
                    label = { Text(stringResource(route.labelResId)) },
                    icon = {
                        val badge = badgeInNavBar[route]
                        if (badge != null) {
                            BadgedBox(
                                badge = { Badge { Text(badge) } }
                            ) { icon() }
                        } else { icon() }
                    }
                )
            }
        } }
    ) { innerPadding ->
        // Main screen content
        NavHost(
            navController = navController,
            startDestination = MainRoutes.HOME.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainRoutes.HOME.name) {
                Column {
                    Text("Home")
                    Button(onClick = onNavigateToMap) {
                        Text("Go to map")
                    }
                }
            }
            composable(MainRoutes.ORDER.name) {
                Text("Order")
            }
            composable(MainRoutes.FAVORITE.name) {
                Text("Favorite")
            }
            composable(MainRoutes.NOTIFICATION.name) {
                Text("Notification")
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPrev() {
    MainScreen({})
}