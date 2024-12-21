package com.ilikeincest.food4student.screen.main_page.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.component.BetterPullToRefreshBox
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.Notification
import com.ilikeincest.food4student.screen.main_page.notification.component.NotificationItem
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@Composable
fun NotificationScreen(
    nestedScrollConnection: NestedScrollConnection,
    viewModel: NotificationScreenViewModel = hiltViewModel()
) {
    val notifications = viewModel.notifications
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val onRefresh = { viewModel.refreshNotifications() }
    val showNewNotification by viewModel.newNotificationAvailable.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val alreadyInit by viewModel.alreadyInit.collectAsState()
    LaunchedEffect(Unit) {
        if (alreadyInit) return@LaunchedEffect
        viewModel.refreshNotifications()
    }

    NotificationScreenContent(
        notifications = notifications,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        nestedScrollConnection = nestedScrollConnection,
        showNewNotification = showNewNotification,
        onSeenNewNotification = { viewModel.newNotificationAlreadySeen() },
        errorMessage = errorMessage,
        onDismissError = { viewModel.dismissErrorDialog() }
    )
}

@Composable
private fun NotificationScreenContent(
    notifications: List<Notification>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    nestedScrollConnection: NestedScrollConnection,
    showNewNotification: Boolean,
    onSeenNewNotification: () -> Unit,
    errorMessage: String,
    onDismissError: () -> Unit,
) {
    val state = rememberLazyListState()
    val isScrolledToTop by remember { derivedStateOf {
        state.firstVisibleItemIndex == 0
                && state.firstVisibleItemScrollOffset == 0
    } }

    if (errorMessage.isNotEmpty()) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = onDismissError
        )
    }

    BetterPullToRefreshBox(
        lazyListState = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        if (notifications.isEmpty()) {
            Text(
                "Không có thông báo. Tạm thời là thế ( ͡° ͜ʖ ͡°)",
                Modifier.align(Alignment.Center)
            )
            return@BetterPullToRefreshBox
        }
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)) {
            items(notifications, key = { it.id }) {
                Column(Modifier.fillMaxWidth()) {
                    // TODO: add clickable behaviors (open, context menus)
                    NotificationItem(
                        imageModel = it.image,
                        title = it.title,
                        content = it.content,
                        timestamp = it.timestamp,
                        isUnread = it.isUnread,
                        modifier = Modifier.fillMaxWidth()
                            .clickable {  }
                    )
                    HorizontalDivider()
                }
            }
        }
        if (showNewNotification) {
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(isScrolledToTop) {
                if (isScrolledToTop) {
                    onSeenNewNotification()
                }
            }
            ElevatedSuggestionChip(
                onClick = {
                    coroutineScope.launch {
                        state.animateScrollToItem(0)
                    }
                },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Có thông báo mới")
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowUpward, null, Modifier.size(16.dp))
                    }
                },
                elevation = SuggestionChipDefaults.suggestionChipElevation(12.dp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Prev() { ScreenPreview {
    NotificationScreenContent(
        notifications = List(10) {
            Notification(
                id = it.toString(),
                image = null,
                title = "Phúc Long",
                content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
                timestamp = Clock.System.now(),
                isUnread = listOf(0, 2, 3, 8).contains(it)
            )
        },
        isRefreshing = false,
        onRefresh = {},
        nestedScrollConnection = TopAppBarDefaults.pinnedScrollBehavior().nestedScrollConnection,
        showNewNotification = true,
        onSeenNewNotification = {},
        errorMessage = "",
        onDismissError = {},
    )
} }

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PrevEmpty() { ScreenPreview {
    NotificationScreenContent(
        notifications = listOf(),
        isRefreshing = false,
        onRefresh = {},
        nestedScrollConnection = TopAppBarDefaults.pinnedScrollBehavior().nestedScrollConnection,
        showNewNotification = true,
        onSeenNewNotification = {},
        errorMessage = "",
        onDismissError = {},
    )
} }