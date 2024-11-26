package com.ilikeincest.food4student.screen.main_page.order.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.AsyncImageOrMonogram
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview
import com.ilikeincest.food4student.model.OrderItem
import com.ilikeincest.food4student.util.formatPrice
import java.time.LocalDate
import java.util.Locale

@Composable
fun OrderCard(
    id: String, // To be configured with db apis
    shopName: String,
    shopId: String, // For extra lookup
    shopImageUrl: String,
    date: LocalDate,
    orderItems: List<OrderItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
        ) {
            Text("Đơn hàng",
                style = typography.titleMedium,
                modifier = Modifier.alignByBaseline()
            )
            Spacer(Modifier.width(6.dp))
            Text("#${id}",
                style = typography.bodyLarge,
                color = colorScheme.onSurfaceVariant,
                modifier = Modifier.alignByBaseline()
            )

            Spacer(Modifier.weight(1f))

            val locale = Locale.forLanguageTag("vi-VN")
            // format month as fixed 2digits
            val monthAsStr = String.format(locale, "%02d", date.monthValue)
            val dateStr = "${date.dayOfMonth}/${monthAsStr}/${date.year}"
            Text(dateStr,
                style = typography.bodyMedium,
                modifier = Modifier.alignByBaseline()
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
        ) {
            // peak ui code
            // the chevron will stay after the text,
            // but text will ellipses and chevron will still be visible
            AsyncImageOrMonogram(
                model = shopImageUrl,
                name = shopName,
                contentDescription = "Shop avatar"
            )
            Spacer(Modifier.width(10.dp))
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    shopName, style = typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            var totalPrice = 0
            for (it in orderItems) {
                totalPrice += it.price
                OrderItemCard(
                    imageModel = it.imageUrl,
                    title = it.name,
                    notes = it.note,
                    price = it.price,
                    quantity = it.quantity
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tổng cộng:", style = typography.titleMedium)
                Text(formatPrice(totalPrice), style = typography.titleLarge.copy(fontWeight = FontWeight(600)))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun OrderPreview() {
    ComponentPreview {
        OrderCard(
            id = "5ea765ds",
            date = LocalDate.of(1969, 2, 28),
            shopName = "Phúc Long",
            shopId = "fuck u",
            shopImageUrl = "",
            orderItems = listOf(
                OrderItem("Trà sữa Phô mai tươi", "Size S - không đá", 2, 54_000,  "https://unsplash.com/photos/IaPlDU14Oig/download?ixid=M3wxMjA3fDB8MXxhbGx8OXx8fHx8fDJ8fDE3MjY1NTQ2MDN8&force=true&w=640"),
                OrderItem("Trà sữa Phô mai tươi 2", "Size S - không đá", 2, 54_000,  "https://unsplash.com/photos/IaPlDU14Oig/download?ixid=M3wxMjA3fDB8MXxhbGx8OXx8fHx8fDJ8fDE3MjY1NTQ2MDN8&force=true&w=640"),
                OrderItem("Trà sữa Phô mai tươi nhưng mà nó siêu dài cmnl lmao", "Size S - không đá", 2, 54_000,  "https://unsplash.com/photos/IaPlDU14Oig/download?ixid=M3wxMjA3fDB8MXxhbGx8OXx8fHx8fDJ8fDE3MjY1NTQ2MDN8&force=true&w=640"),
            ),
            modifier = Modifier.width(368.dp)
        )
    }
}