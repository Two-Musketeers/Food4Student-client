package com.ilikeincest.food4student.screen.main_page.order.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.AsyncImageOrMonogram
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview
import com.ilikeincest.food4student.dto.RatingDto
import com.ilikeincest.food4student.model.OrderItem
import com.ilikeincest.food4student.model.OrderStatus
import com.ilikeincest.food4student.util.formatPrice
import com.ilikeincest.food4student.util.timeFrom
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

@Composable
fun OrderCard(
    id: String,
    onReview: (star: Int, comment: String) -> Unit,
    restaurantName: String,
    shopImageUrl: String,
    createdAt: Instant,
    rating: RatingDto?,
    isReviewable: Boolean,
    orderItems: List<OrderItem>,
    onNavigateToRestaurant: () -> Unit,
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
                Text(formatPrice(totalPrice), style = typography.titleLarge.copy(fontWeight = FontWeight(600)))
            }
            if (isReviewable && rating == null) {
                HorizontalDivider(Modifier.fillMaxWidth())
                Text("Thêm đánh giá ngay!", style = typography.titleLarge)
                var selectedStar by remember { mutableIntStateOf(5) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Số sao", Modifier.padding(end = 16.dp))
                    OutlinedIconButton(onClick = { if (selectedStar != 1) selectedStar-- },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Remove, "Remove from cart")
                    }
                    Text(selectedStar.toString(),
                        style = typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 38.dp)
                            .padding(horizontal = 8.dp)
                    )
                    FilledIconButton(onClick = { if (selectedStar != 5) selectedStar++ },
                        shape = RoundedCornerShape(4.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = colorScheme.tertiary,
                            contentColor = colorScheme.onTertiary
                        ),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, "Add to cart")
                    }
                }
                var comment by remember { mutableStateOf("") }
                TextField(
                    value = comment, onValueChange = { comment = it },
                    label = { Text("Comment") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button({ onReview(selectedStar, comment) }) {
                    Text("Đánh giá")
                }
            } else if (rating != null) {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            for (i in 1..rating.stars) {
                                Icon(Icons.Default.Star, null,
                                    Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("\"${rating.comment}\"")
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
        OrderCard(
            id = "5ea765ds-123123-asdasd12edqda-123edsa",
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
            rating = RatingDto(
                id = "",
                stars = 4,
                comment = "asdasd asd asd as das d sad sadsadasd"
            ),
            isReviewable = true,
            onReview = {_,_->},
            onNavigateToRestaurant = {},
            modifier = Modifier.width(368.dp)
        )
    }
}