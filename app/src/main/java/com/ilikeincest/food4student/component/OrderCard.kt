package com.ilikeincest.food4student.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ilikeincest.food4student.model.OrderItem
import com.ilikeincest.food4student.util.formatPrice
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun OrderCard(
    id: String, // To be configured with db apis
    shopName: String,
    shopId: String, // For extra lookup
    shopImage: @Composable (modifier: Modifier) -> Unit,
    date: LocalDate,
    orderItems: List<OrderItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
        ) {
            Row {
                Text("Đơn hàng", style = typography.titleMedium)
                Spacer(Modifier.width(6.dp))
                Text("#${id}",
                    style = typography.bodyLarge,
                    color = colorScheme.onSurfaceVariant
                )
            }
            val locale = Locale.forLanguageTag("vi-VN")
            val dateStr = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale))
            Text(dateStr, style = typography.bodyMedium)
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
            shopImage(Modifier.size(28.dp))
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
                    // TODO: add item image link to model
                    image = rememberAsyncImagePainter("https://unsplash.com/photos/IaPlDU14Oig/download?ixid=M3wxMjA3fDB8MXxhbGx8OXx8fHx8fDJ8fDE3MjY1NTQ2MDN8&force=true&w=640"),
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
                Text(formatPrice(totalPrice), style = typography.titleLarge)
            }
        }
    }
}

@Preview
@Composable
private fun OrderPreview() {
    OrderCard(
        id = "5ea765ds",
        date = LocalDate.of(1969, 2, 28),
        shopName = "Phúc Long",
        shopId = "fuck u",
        shopImage = { MonogramAvatar(initials = "PL", it) },
        orderItems = listOf(
            OrderItem("Trà sữa Phô mai tươi", "Size S - không đá", 2, 54_000),
            OrderItem("Trà sữa Phô mai tươi 2", "Size S - không đá", 2, 54_000),
            OrderItem("Trà sữa Phô mai tươi 3", "Size S - không đá", 2, 54_000),
        ),
        modifier = Modifier.width(368.dp)
    )
}