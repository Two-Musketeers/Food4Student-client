package com.ilikeincest.food4student.screen.main_page

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.screen.main_page.favorite.FavoriteScreen
import com.ilikeincest.food4student.screen.main_page.home.HomeScreen
import com.ilikeincest.food4student.screen.main_page.notification.NotificationScreen
import com.ilikeincest.food4student.screen.main_page.notification.NotificationScreenViewModel
import com.ilikeincest.food4student.screen.main_page.order.OrderScreen

internal val defaultRoute = MainRoutes.HOME

internal enum class MainRoutes(
    @StringRes val labelResId: Int,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    HOME(
        R.string.home_screen_label,
        Icons.Outlined.Fastfood,
        Icons.Filled.Fastfood
    ),
    ORDER(
        R.string.order_screen_label,
        Icons.AutoMirrored.Outlined.ReceiptLong,
        Icons.AutoMirrored.Filled.ReceiptLong
    ),
    FAVORITE(
        R.string.favorite_screen_label,
        // This is utterly stupid. Fuck google. And their stupid ass everything.
        // Why the fuck is Icons.Outlined.Bookmark the same as Icons.Filled.Bookmark???
        // Then what the fuck does Outlined even mean???
        Icons.Outlined.BookmarkBorder,
        Icons.Filled.Bookmark
    ),
    NOTIFICATION(
        R.string.notification_screen_label,
        Icons.Outlined.Notifications,
        Icons.Filled.Notifications
    ),
}

@Composable
internal fun MainScreenPageGraph(
    currentRoute: MainRoutes,
    onNavigateToShippingLocation: () -> Unit,
    scrollConnection: NestedScrollConnection,
    modifier: Modifier = Modifier
) {
    // view models are declared here to force their scope to the root NavHost
    val notificationViewModel = hiltViewModel<NotificationScreenViewModel>()

    val inTransition = fadeIn(tween(durationMillis = 250))
    val outTransition = fadeOut(tween(durationMillis = 250))
    AnimatedContent(
        targetState = currentRoute,
        modifier = modifier,
        transitionSpec = {
            inTransition togetherWith outTransition using SizeTransform(clip = false)
        },
        label = "Main screen swap page"
    ) {
        when (it) {
            MainRoutes.HOME ->
                HomeScreen(onNavigateToShippingLocation)
            MainRoutes.ORDER ->
                OrderScreen()
            MainRoutes.FAVORITE ->
                FavoriteScreen()
            MainRoutes.NOTIFICATION ->
                NotificationScreen(scrollConnection, notificationViewModel)
        }
    }
}