package com.ilikeincest.food4student.screen.main_page.notification.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.util.timeFrom
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun NotificationItem(
    imageModel: Any?,
    title: String,
    content: String,
    timestamp: Instant,
    isUnread: Boolean,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableStateOf(Clock.System.now()) }
    // update currentTime every 10 seconds to save resources
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (true) {
                delay(10.seconds)
                currentTime = Clock.System.now()
            }
        }
    }
    val dateTime = timestamp.timeFrom(currentTime)

    val imageSize = 76.dp

    val bgColor =
        if (isUnread) colorScheme.surface
        else colorScheme.surfaceVariant

    val textColor =
        if (isUnread) colorScheme.onSurface
        else colorScheme.onSurfaceVariant

    Surface(color = bgColor) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = imageModel, contentDescription = "Notification image",
                placeholder = ColorPainter(colorScheme.primaryContainer),
                error = ColorPainter(colorScheme.error),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(16.dp))
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Text(title,
                        style = typography.titleMedium, color = textColor,
                        modifier = Modifier.weight(1f)
                    )
                    if (isUnread) {
                        Badge(Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 6.dp)
                            .size(10.dp)
                        )
                    }
                }
                Text(content, style = typography.bodyMedium, color = textColor)
                Text(dateTime, style = typography.bodySmall, color = colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Preview
@Composable
private fun ReadPreview() {
    NotificationItem(
        imageModel = R.drawable.ic_launcher_background,
        title = "Phúc Long",
        content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
        timestamp = Clock.System.now().minus(1.hours),
        isUnread = false,
    )
}

@Preview
@Composable
private fun UnreadPreview() {
    NotificationItem(
        imageModel = R.drawable.ic_launcher_background,
        title = "Phúc Long nhưng mà nó dài ác trời ơi hỡi",
        content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
        timestamp = Clock.System.now().minus(5.minutes),
        isUnread = true,
    )
}