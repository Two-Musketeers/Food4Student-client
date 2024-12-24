package com.ilikeincest.food4student.screen.restaurant.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ilikeincest.food4student.screen.restaurant.detail.CartItem
import com.ilikeincest.food4student.screen.restaurant.detail.RestaurantDetailViewModel
import com.ilikeincest.food4student.util.formatPrice

@Composable
fun CartItemRow(
    cartItem: CartItem,
    viewModel: RestaurantDetailViewModel
) {
    Column(
        modifier = Modifier.Companion.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            AsyncImage(
                model = cartItem.foodItem.foodItemPhotoUrl,
                contentDescription = "Food Item",
                contentScale = ContentScale.Companion.Crop,
                modifier = Modifier.Companion
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.Companion.width(16.dp))
            Column(
                modifier = Modifier.Companion.weight(1f)
            ) {
                Text(
                    text = cartItem.foodItem.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Companion.SemiBold)
                )
                if (cartItem.selectedVariations.isNotEmpty()) {
                    Text(
                        text = "Options: ${
                            viewModel.formatSelectedVariations(
                                cartItem.selectedVariations,
                                cartItem.foodItem
                            )
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "Quantity: ${cartItem.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = formatPrice((cartItem.foodItem.basePrice + getVariationExtra(cartItem)) * cartItem.quantity),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Companion.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

private fun getVariationExtra(cartItem: CartItem): Int {
    return cartItem.selectedVariations.entries.sumOf { (variationId, optionIds) ->
        optionIds.sumOf { optionId ->
            cartItem.foodItem.variations.find { it.id == variationId }
                ?.variationOptions?.find { it.id == optionId }
                ?.priceAdjustment ?: 0
        }
    }
}