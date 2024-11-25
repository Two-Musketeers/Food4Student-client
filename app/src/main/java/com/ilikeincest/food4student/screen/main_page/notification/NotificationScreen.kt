package com.ilikeincest.food4student.screen.main_page.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.screen.main_page.notification.component.NotificationItem
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.Notification
import java.time.LocalDateTime

@Composable
fun NotificationScreen(modifier: Modifier = Modifier) {
    // TODO: move to viewmodel
    val imagePainter = painterResource(R.drawable.ic_launcher_background)
    val notifications = remember { listOf(
        Notification(
            image = imagePainter,
            title = "Phúc Long",
            content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
            timestamp = LocalDateTime.now(),
            isUnread = true
        ),
        Notification(
            image = imagePainter,
            title = "Phúc Long",
            content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
            timestamp = LocalDateTime.now(),
            isUnread = false
        ),
        Notification(
            image = imagePainter,
            title = "Phúc Long",
            content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
            timestamp = LocalDateTime.now(),
            isUnread = true
        ),
        Notification(
            image = imagePainter,
            title = "Phúc Long",
            content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
            timestamp = LocalDateTime.now(),
            isUnread = true
        )
    ) }
    LazyColumn(modifier) {
        items(notifications) {
            Column(Modifier.fillMaxWidth()) {
                NotificationItem(
                    image = it.image,
                    title = it.title,
                    content = it.content,
                    timestamp = it.timestamp,
                    isUnread = it.isUnread,
                    modifier = modifier.fillMaxWidth()
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview
@Composable
private fun Notification() { ScreenPreview {
    NotificationScreen()
} }