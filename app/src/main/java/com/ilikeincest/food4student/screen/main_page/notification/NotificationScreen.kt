package com.ilikeincest.food4student.screen.main_page.notification

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ilikeincest.food4student.component.BroadcastReceiver
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.Notification
import com.ilikeincest.food4student.screen.main_page.notification.component.NotificationItem
import java.time.LocalDateTime
import kotlin.random.Random

@Composable
fun NotificationScreen(
    viewModel: NotificationScreenViewModel
) {
    // TODO: move to viewmodel
    val notifications = viewModel.notifications
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val onRefresh = { viewModel.refreshNotifications() }

    val alreadyInit by viewModel.alreadyInit.collectAsState()
    LaunchedEffect(Unit) {
        if (alreadyInit) return@LaunchedEffect
        Log.d("DEBUG", "fuck you")
        viewModel.refreshNotifications()
    }

    BroadcastReceiver("com.ilikeincest.food4student.NEW_MESSAGE") {
        val title = it?.getStringExtra("title")
        val message = it?.getStringExtra("message")
        val imageUrl = it?.getStringExtra("imageUrl")

        // Do something with the message
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
        notifications,
        isRefreshing,
        onRefresh
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationScreenContent(
    notifications: List<Notification>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        state = rememberPullToRefreshState(),
        onRefresh = onRefresh
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
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
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

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
        onRefresh = {}
    )
} }