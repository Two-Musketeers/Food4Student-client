package com.ilikeincest.food4student.screen.restaurant.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilikeincest.food4student.model.FoodItem
import com.ilikeincest.food4student.model.Variation
import com.ilikeincest.food4student.model.VariationOption
import com.ilikeincest.food4student.screen.restaurant.detail.RestaurantDetailViewModel
import com.ilikeincest.food4student.util.formatPrice
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToCartSheet(
    item: FoodItem,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RestaurantDetailViewModel
) {
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val coroutine = rememberCoroutineScope()
    val allSelectedVariations by viewModel.selectedVariations.collectAsState()
    val itemSelectedVariations = allSelectedVariations[item.id] ?: emptyMap()

    val canAdd by remember(itemSelectedVariations) {
        derivedStateOf { viewModel.isSelectedVariationsValid(item.id) }
    }

    LaunchedEffect(item.id) {
        viewModel.isSelectedVariationsValid(item.id)
    }
    ModalBottomSheet(
        sheetGesturesEnabled = false,
        sheetState = state,
        dragHandle = null,
        onDismissRequest = onDismiss,
        contentWindowInsets = { WindowInsets(0.dp) },
        modifier = modifier,
    ) {
        // visible area the sheet occupies on screen
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val visibleHeight = screenHeight * 0.8f
        Column(
            Modifier
                .fillMaxWidth()
                .requiredSizeIn(maxHeight = visibleHeight)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text(
                    "Thêm món mới",
                    style = typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = {
                        coroutine.launch {
                            state.hide()
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.Close, "Close order sheet")
                }
            }
            HorizontalDivider(
                Modifier
                    .padding(top = 4.dp)
                    .alpha(0.4f)
            )
            var quantity by rememberSaveable { mutableStateOf(1) }
            FoodItemCard(
                item = item,
                modifier = Modifier.padding(16.dp),
                onDecreaseInCart = {
                    if (quantity > 1) quantity--
                },
                onIncreaseInCart = { quantity++ },
                inCartCount = quantity
            )
            LazyColumn(Modifier.fillMaxWidth().weight(1f)) {
                item.variations.forEach {
                    variationSection(it,
                        checkedOptionId = itemSelectedVariations[it.id] ?: emptyList(),
                        onCheckedOptionChange = { optionId ->
                            viewModel.selectVariation(item.id, it.id, optionId)
                        }
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(colorScheme.surfaceContainer)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                // calculate total price
                val variationExtra = item.variations.sumOf { variation ->
                    val selectedOptionIds = itemSelectedVariations[variation.id] ?: emptyList()
                    variation.variationOptions
                        .filter { selectedOptionIds.contains(it.id) }
                        .sumOf { it.priceAdjustment }
                }
                val totalPrice = (item.basePrice + variationExtra) * quantity
                Text(
                    formatPrice(totalPrice),
                    style = typography.titleLarge.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W800
                    ),
                    color = colorScheme.error
                )
                Spacer(Modifier.weight(1f))
                Button(
                    enabled = canAdd,
                    onClick = {
                        viewModel.addToCart(item, itemSelectedVariations, quantity)
                        coroutine.launch {
                            state.hide()
                            onDismiss()
                        }
                    }
                ) {
                    Text("Thêm vào giỏ hàng")
                }
            }
        }
    }
}

//@Preview
//@Composable
//private fun Prev() {
//    ScreenPreview {
//        var show by remember { mutableStateOf(true) }
//        Button(onClick = {show = true}, Modifier.padding(top = 64.dp)) {
//            Text("open sheet")
//        }
//        if (show) {
//            AddToCartSheet(
//                seedFood(),
//                {show = false},
//                {}
//            )
//        }
//    }
//}

private fun seedFood(): FoodItem {
    return FoodItem(
        id = "",
        name = "Hồng Trà Kem Tươi",
        description = "Kem có tan chảy",
        foodItemPhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR58QY4pAtehHlOZtYU0gDSbTABNIsxy2z_gQ&s",
        basePrice = 23000,
        variations = List(5) {
            Variation(
                id = Random.nextInt().toString(),
                name = "Size",
                minSelect = 1,
                maxSelect = 1,
                variationOptions = listOf(
                    VariationOption(
                        id = Random.nextInt().toString(),
                        name = "Size S",
                        priceAdjustment = 0
                    ),
                    VariationOption(
                        id = Random.nextInt().toString(),
                        name = "Size M",
                        priceAdjustment = 3000
                    ),
                    VariationOption(
                        id = Random.nextInt().toString(),
                        name = "Size L",
                        priceAdjustment = 6000
                    )
                )
            )
        }
    )
}