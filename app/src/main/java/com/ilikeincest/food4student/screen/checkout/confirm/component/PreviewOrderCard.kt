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
import com.ilikeincest.food4student.screen.restaurant.detail.CartItem
import com.ilikeincest.food4student.util.formatPrice
import com.ilikeincest.food4student.util.timeFrom
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

@Composable
fun PreviewOrderCard(
    restaurantName: String,
    shopImageUrl: String?,
    orderItems: List<CartItem>,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = colorScheme.primaryContainer,
            contentColor = colorScheme.onPrimaryContainer
        ),
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
                AsyncImageOrMonogram(
                    model = shopImageUrl,
                    name = restaurantName,
                    contentDescription = "Shop avatar"
                )
                Spacer(Modifier.width(10.dp))
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        restaurantName, style = typography.titleLarge,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                var totalPrice = 0
                for (it in orderItems) {
                    val price = (it.foodItem.basePrice + it.selectedVariations.entries.sumOf { (variationId, optionIds) ->
                        optionIds.sumOf { optionId ->
                            it.foodItem.variations.find { it.id == variationId }
                                ?.variationOptions?.find { it.id == optionId }
                                ?.priceAdjustment ?: 0
                        }
                    })
                    val mergedVariations = it.selectedVariations.entries.joinToString("\n") { (variationId, optionIds) ->
                        val variation = it.foodItem.variations.find { it.id == variationId }
                        val options = variation?.variationOptions?.filter { it.id in optionIds }
                        "${variation?.name}: ${options?.joinToString { it.name } ?: ""}"
                    }

                    OrderItemCard(
                        imageModel = it.foodItem.foodItemPhotoUrl,
                        title = it.foodItem.name,
                        notes = mergedVariations,
                        price = price,
                        quantity = it.quantity
                    )
                    totalPrice += price
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
