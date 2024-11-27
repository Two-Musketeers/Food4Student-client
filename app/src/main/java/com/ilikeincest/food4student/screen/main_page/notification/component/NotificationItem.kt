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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ilikeincest.food4student.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun NotificationItem(
    imageModel: Any?,
    title: String,
    content: String,
    timestamp: LocalDateTime,
    isUnread: Boolean,
    modifier: Modifier = Modifier
) {
    // this is janky. don't do this. please.
    val locale = Locale.forLanguageTag("vi-VN")
    val date = timestamp.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale))
    val time = timestamp.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale))
    val dateTime = "$date $time"

    val imageSize = 76.dp

    val bgColor =
        if (isUnread) colorScheme.surface
        else colorScheme.surfaceVariant

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
                val textColor =
                    if (isUnread) colorScheme.onSecondaryContainer
                    else colorScheme.onSurface
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
        imageModel = painterResource(id = R.drawable.ic_launcher_background),
        title = "Phúc Long",
        content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
        timestamp = LocalDateTime.now(),
        isUnread = false,
    )
}

@Preview
@Composable
private fun UnreadPreview() {
    NotificationItem(
        imageModel = painterResource(id = R.drawable.ic_launcher_background),
        title = "Phúc Long nhưng mà nó dài ác trời ơi hỡi",
        content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
        timestamp = LocalDateTime.now(),
        isUnread = true,
    )
}