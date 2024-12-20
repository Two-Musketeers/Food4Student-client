package com.ilikeincest.food4student.screen.main_page

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import com.ilikeincest.food4student.component.BroadcastReceiver
import com.ilikeincest.food4student.model.Notification
import com.ilikeincest.food4student.screen.main_page.favorite.FavoriteScreen
import com.ilikeincest.food4student.screen.main_page.home.HomeScreen
import com.ilikeincest.food4student.screen.main_page.notification.NotificationScreen
import com.ilikeincest.food4student.screen.main_page.notification.NotificationScreenViewModel
import com.ilikeincest.food4student.screen.main_page.order.OrderScreen
import kotlinx.datetime.Clock
import kotlin.random.Random

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
    onNavigateToRestaurant: (id: String) -> Unit,
    scrollConnection: NestedScrollConnection,
    modifier: Modifier = Modifier
) {
    // Allow receiving new notifications and appending it to list
    // so that we dont have to full reload everytime we navigate to notification screen
    val notificationViewModel = hiltViewModel<NotificationScreenViewModel>()
    BroadcastReceiver("com.ilikeincest.food4student.NEW_MESSAGE") {
        val title = it?.getStringExtra("title")
        val message = it?.getStringExtra("message")
        val imageUrl = it?.getStringExtra("imageUrl")

        if (message == null) throw Error("Message cant be null")

        val newNoti = Notification(
            id = Random.nextInt().toString(),
            image = imageUrl,
            title = title ?: "Thông báo",
            timestamp = Clock.System.now(),
            content = message,
            isUnread = true
        )
        notificationViewModel.addNewNotification(newNoti)
    }

    val inTransition = fadeIn(tween(durationMillis = 250)) + slideInVertically { it / 50 }
    val outTransition = fadeOut(tween(durationMillis = 250))
    AnimatedContent(
        targetState = currentRoute,
        modifier = modifier,
        transitionSpec = {
            inTransition togetherWith outTransition using SizeTransform()
        },
        label = "Main screen swap page"
    ) {
        when (it) {
            MainRoutes.HOME ->
                HomeScreen(
                    onNavigateToShippingLocation,
                    onNavigateToRestaurant
                )
            MainRoutes.ORDER ->
                OrderScreen()
            MainRoutes.FAVORITE ->
                FavoriteScreen()
            MainRoutes.NOTIFICATION ->
                NotificationScreen(scrollConnection)
        }
    }
}