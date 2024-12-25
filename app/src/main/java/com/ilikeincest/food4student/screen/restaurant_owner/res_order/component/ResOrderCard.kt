package com.ilikeincest.food4student.screen.restaurant_owner.res_order.component

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.AsyncImageOrMonogram
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview
import com.ilikeincest.food4student.model.OrderItem
import com.ilikeincest.food4student.model.OrderStatus
import com.ilikeincest.food4student.util.formatPrice
import com.ilikeincest.food4student.util.timeFrom
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

@Composable
fun ResOrderCard(
    id: String,
    status: OrderStatus,
    restaurantName: String,
    shopImageUrl: String,
    createdAt: Instant,
    orderItems: List<OrderItem>,
    address: String,
    onNavigateToRestaurant: () -> Unit,
    onCancel: () -> Unit,
    onApprove: () -> Unit,
    onDelivered: () -> Unit,
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
            Text("#${id.substringBefore('-')}",
                style = typography.bodyLarge,
                color = colorScheme.onSurfaceVariant,
                modifier = Modifier.alignByBaseline()
            )

            Spacer(Modifier.weight(1f))

            val now = remember { Clock.System.now() }
            Text(createdAt.timeFrom(now),
                style = typography.bodyMedium,
                modifier = Modifier.alignByBaseline()
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .clickable { onNavigateToRestaurant() }
        ) {
            // peak ui code
            // the chevron will stay after the text,
            // but text will ellipses and chevron will still be visible
            AsyncImageOrMonogram(
                model = shopImageUrl,
                name = restaurantName,
                contentDescription = "Shop avatar"
            )
            Spacer(Modifier.width(10.dp))
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    restaurantName, style = typography.titleLarge,
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
                ResOrderItemCard(
                    imageModel = it.foodItemPhotoUrl,
                    title = it.foodName,
                    notes = it.variations,
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

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Địa chỉ giao", color = colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(6.dp))
                    Text(address, fontWeight = FontWeight.W600)
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (status in listOf(OrderStatus.Cancelled, OrderStatus.Delivered))
                    return@Row
                if (status == OrderStatus.Pending) {
                    Button(onCancel,
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = colorScheme.error,
                            contentColor = colorScheme.onError
                        )
                    ) {
                        Text("Hủy đơn")
                    }
                    Button(onApprove) {
                        Text("Chấp nhận")
                    }
                }
                if (status == OrderStatus.Approved) {
                    Button(onDelivered) {
                        Text("Đã giao")
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun OrderPreview() {
    ComponentPreview {
        ResOrderCard(
            id = "5ea765ds-123123-asdasd12edqda-123edsa",
            status = OrderStatus.Pending,
            address = "Ký túc xá Khu B",
            createdAt = Clock.System.now() - 20.days,
            restaurantName = "Phúc Long",
            shopImageUrl = "",
            orderItems = List(3) { OrderItem(
                id = "5ea765ds",
                foodName = "Trà sữa Phô mai tươi",
                foodDescription = null,
                price = 54_000,
                quantity = 2,
                foodItemPhotoUrl = "",
                originalFoodItemId = "",
                variations = "Size S - không đá"
            ) },
            onNavigateToRestaurant = {},
            onApprove = {},
            onCancel = {},
            onDelivered = {},
            modifier = Modifier.width(368.dp)
        )
    }
}