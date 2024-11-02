package com.ilikeincest.food4student.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun NotificationItem(
    image: Painter,
    title: String,
    content: String,
    timestamp: LocalDateTime,
    isUnread: Boolean,
    modifier: Modifier = Modifier
) {
    val locale = Locale.forLanguageTag("vi-VN")
    val date = timestamp.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale))
    val time = timestamp.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale))
    val dateTime = "$date $time"

    val imageSize = 76.dp

    val bgColor =
        if (isUnread) colorScheme.secondaryContainer
        else colorScheme.surface

    Surface(color = bgColor) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = image, contentDescription = "Notification image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(imageSize)
                    .width(imageSize)
                    .clip(RoundedCornerShape(16.dp))
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val textColor =
                    if (isUnread) colorScheme.onSecondaryContainer
                    else colorScheme.onSurface
                Text(title, style = typography.titleMedium, color = textColor)
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
        image = painterResource(id = R.drawable.ic_launcher_background),
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
        image = painterResource(id = R.drawable.ic_launcher_background),
        title = "Phúc Long",
        content = "Mời bạn tâm sự chuyện đặt món cùng ShopeeFood và nhận ngay Voucher",
        timestamp = LocalDateTime.now(),
        isUnread = true,
    )
}