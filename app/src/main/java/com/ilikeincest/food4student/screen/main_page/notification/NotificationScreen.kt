package com.ilikeincest.food4student.screen.main_page.notification

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import com.ilikeincest.food4student.component.BroadcastReceiver
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.Notification
import com.ilikeincest.food4student.screen.main_page.notification.component.NotificationItem
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.random.Random

@Composable
fun NotificationScreen(
    nestedScrollConnection: NestedScrollConnection,
    viewModel: NotificationScreenViewModel
) {
    val notifications = viewModel.notifications
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val onRefresh = { viewModel.refreshNotifications() }
    val showNewNotification by viewModel.newNotificationAvailable.collectAsState()

    val alreadyInit by viewModel.alreadyInit.collectAsState()
    LaunchedEffect(Unit) {
        if (alreadyInit) return@LaunchedEffect
        viewModel.refreshNotifications()
    }

    BroadcastReceiver("com.ilikeincest.food4student.NEW_MESSAGE") {
        val title = it?.getStringExtra("title")
        val message = it?.getStringExtra("message")
        val imageUrl = it?.getStringExtra("imageUrl")

        if (message == null) throw Error("Message cant be null")

        val newNoti = Notification(
            id = Random.nextInt().toString(),
            image = imageUrl,
            title = title ?: "Thông báo",
            timestamp = LocalDateTime.now(),
            content = message,
            isUnread = true
        )
        viewModel.addNewNotification(newNoti)
    }

    NotificationScreenContent(
        notifications = notifications,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        nestedScrollConnection = nestedScrollConnection,
        showNewNotification = showNewNotification,
        onSeenNewNotification = { viewModel.newNotificationAlreadySeen() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationScreenContent(
    notifications: List<Notification>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    nestedScrollConnection: NestedScrollConnection,
    showNewNotification: Boolean,
    onSeenNewNotification: () -> Unit,
) {
    val state = rememberLazyListState()
    val isScrolledToTop by remember { derivedStateOf {
        state.firstVisibleItemIndex == 0
                && state.firstVisibleItemScrollOffset == 0
    } }

    val pullState = rememberPullToRefreshState()
    Box(
        modifier = Modifier.pullToRefresh(
            state = pullState,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            enabled = isScrolledToTop
        ),
    ) {
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
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = pullState
        )
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
                timestamp = LocalDateTime.now(),
                isUnread = listOf(0, 2, 3, 8).contains(it)
            )
        },
        isRefreshing = false,
        onRefresh = {},
        nestedScrollConnection = TopAppBarDefaults.pinnedScrollBehavior().nestedScrollConnection,
        showNewNotification = true,
        onSeenNewNotification = {}
    )
} }