package com.ilikeincest.food4student.screen.main_page

import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.screen.main_page.favorite.FavoriteScreen
import com.ilikeincest.food4student.screen.main_page.home.HomeScreen
import com.ilikeincest.food4student.screen.main_page.notification.NotificationScreen
import com.ilikeincest.food4student.screen.main_page.order.OrderScreen

internal enum class MainRoutes(
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
internal fun MainScreenNavGraph(
    navController: NavHostController,
    onNavigateToShippingLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val inTransition = fadeIn(tween(durationMillis = 250))
    val outTransition = fadeOut(tween(durationMillis = 250))
    NavHost(
        navController = navController,
        startDestination = MainRoutes.HOME.name,
        enterTransition = { inTransition },
        popEnterTransition = { inTransition },
        exitTransition = { outTransition },
        popExitTransition = { outTransition },
        modifier = modifier
    ) {
        composable(MainRoutes.HOME.name) {
            HomeScreen(onNavigateToShippingLocation)
        }
        composable(MainRoutes.ORDER.name) {
            OrderScreen()
        }
        composable(MainRoutes.FAVORITE.name) {
            FavoriteScreen()
        }
        composable(MainRoutes.NOTIFICATION.name) {
            NotificationScreen()
        }
    }
}