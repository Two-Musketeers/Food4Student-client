package com.ilikeincest.food4student.screen.restaurant.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.screen.restaurant.detail.RestaurantDetailViewModel
import com.ilikeincest.food4student.util.formatPrice
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    viewModel: RestaurantDetailViewModel,
    onDismiss: () -> Unit,
) {
    val coroutine = rememberCoroutineScope()
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        sheetGesturesEnabled = false,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null
    ) {
        // visible area the sheet occupies on screen
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val visibleHeight = screenHeight * 0.8f
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .requiredSizeIn(maxHeight = visibleHeight)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text(
                    text = "Giỏ hàng của bạn",
                    style = typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = {
                        coroutine.launch {
                            sheetState.hide()
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.Close, "Close Shopping Cart")
                }
            }
            HorizontalDivider(
                Modifier
                    .padding(top = 4.dp)
                    .alpha(0.4f)
            )
            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(cartItems, key = { it.foodItem.id }) { cartItem ->
                    // Example:
                    FoodItemCard(
                        item = cartItem.foodItem.copy(
                            // Replace description with variation info or use a custom layout
                            description = cartItem.selectedVariations.entries.joinToString { (variationId, optionIds) ->
                                val variation = cartItem.foodItem.variations.find { it.id == variationId }
                                val options = variation?.variationOptions?.filter { it.id in optionIds }
                                "${variation?.name}: ${options?.joinToString { it.name } ?: ""}"
                            },
                            basePrice = (cartItem.foodItem.basePrice + cartItem.selectedVariations.entries.sumOf { (variationId, optionIds) ->
                                optionIds.sumOf { optionId ->
                                    cartItem.foodItem.variations.find { it.id == variationId }
                                        ?.variationOptions?.find { it.id == optionId }
                                        ?.priceAdjustment ?: 0
                                }
                            })
                        ),
                        onDecreaseInCart = { viewModel.decreaseCartItem(cartItem.foodItem, cartItem.selectedVariations) },
                        onIncreaseInCart = { viewModel.addToCart(cartItem.foodItem, cartItem.selectedVariations, 1) },
                        inCartCount = cartItem.quantity,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Tổng tiền:",
                    style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatPrice(totalPrice),
                    style = typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary
                    )
                )
            }
            Button(
                onClick = {
                    // TODO: Implement checkout functionality
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                enabled = cartItems.isNotEmpty()
            ) {
                Text("Đặt hàng")
            }
        }
    }
}