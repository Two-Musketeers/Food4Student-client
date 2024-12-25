package com.ilikeincest.food4student.screen.checkout.confirm.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import com.ilikeincest.food4student.screen.main_page.order.component.OrderItemCard
import com.ilikeincest.food4student.util.formatPrice
import com.ilikeincest.food4student.util.timeFrom
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

@Composable
fun PreviewOrderCard(
    restaurantName: String,
    shopImageUrl: String,
    orderItems: List<OrderItem>,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp, top = 12.dp)
        ) {
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
                    OrderItemCard(
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
                    Text(
                        formatPrice(totalPrice),
                        style = typography.titleLarge.copy(fontWeight = FontWeight(600))
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun OrderPreview() {
    ComponentPreview {
        PreviewOrderCard(
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
            modifier = Modifier.width(368.dp).padding(16.dp)
        )
    }
}