package com.ilikeincest.food4student.screen.restaurant.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview
import com.ilikeincest.food4student.model.FoodItem
import com.ilikeincest.food4student.util.formatPrice

@Composable
fun FoodItemCard(
    item: FoodItem,
    onIncreaseInCart: () -> Unit,
    modifier: Modifier = Modifier,
    onDecreaseInCart: (() -> Unit)? = null,
    inCartCount: Int = 0,
) {
    Row(modifier.fillMaxWidth()) {
        AsyncImage(
            model = item.foodItemPhotoUrl,
            contentDescription = "food item cover",
            placeholder = ColorPainter(colorScheme.primaryContainer),
            error = ColorPainter(colorScheme.error),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(Modifier.width(16.dp))
        Column(
            Modifier
                .defaultMinSize(minHeight = 100.dp)
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            Text(item.name, style = typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.W600
            ))
            if (item.description != null) {
                Text(item.description,
                    maxLines = 3, overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.height(8.dp))
            Spacer(Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(formatPrice(item.basePrice),
                    style = typography.titleLarge.copy(
                        fontWeight = FontWeight.W800
                    ),
                    color = colorScheme.primary
                )
                Spacer(Modifier.weight(1f))
                if (inCartCount != 0) {
                    if (onDecreaseInCart != null) {
                        OutlinedIconButton(onClick = onDecreaseInCart,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, "Remove from cart")
                        }
                    }
                    Text(inCartCount.toString(),
                        style = typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 38.dp)
                            .padding(horizontal = 8.dp)
                    )
                }
                FilledIconButton(onClick = onIncreaseInCart,
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
        }
    }
}

@Preview
@Composable
private fun Prev() {
    ComponentPreview {
        FoodItemCard(
            FoodItem(
                id = "",
                name = "Hồng Trà Kem Tươi",
                description = "Kem có tan chảy",
                foodItemPhotoUrl = "https://cc",
                basePrice = 28000,
                variations = listOf()
            ), {},
            Modifier
                .padding(16.dp)
                .width(400.dp)
        )
    }
}

@Preview
@Composable
private fun PrevLong() {
    ComponentPreview {
        FoodItemCard(
            FoodItem(
                id = "",
                name = "Hồng Trà Kem Tươi",
                description = "Kem có tan chảy No feugait ullamcorper elit diam justo. Elitr vel in invidunt in esse tincidunt et dolore nulla lorem vero laoreet vero kasd aliquyam gubergren sanctus feugiat. Est amet et aliquip aliquyam lobortis dolore esse dolor duo lorem rebum. Dolor sed magna magna sit erat erat et. No voluptua diam aliquyam dolor dolores lorem sed labore. Sit eros lorem dolor cum ut takimata est accusam diam dolore labore. Aliquyam et tempor dolor erat blandit at eos invidunt. Est et sanctus justo sit blandit labore consequat et duo. Rebum amet vulputate magna dolor et in feugait tation diam tempor ullamcorper. Diam in rebum tempor odio sed sea eos ipsum dolore vero diam consequat. Sanctus ipsum ullamcorper accusam stet et et at sed aliquam et ea. Dolore et at dolore labore te et kasd consetetur. Rebum magna et facilisis. Et stet et lorem amet ut tempor takimata dolor vero sea vel dolores sit. No accusam est ad elitr erat accusam eirmod vero dolores velit sed adipiscing.",
                foodItemPhotoUrl = "https://cc",
                basePrice = 28000,
                variations = listOf()
            ), {},
            Modifier
                .padding(16.dp)
                .width(400.dp),
            onDecreaseInCart = {},
            inCartCount = 3
        )
    }
}

@Preview
@Composable
private fun PrevLong2() {
    ComponentPreview {
        FoodItemCard(
            FoodItem(
                id = "",
                name = "Hồng Trà Kem Tươi",
                description = "Kem có tan chảy No feugait ullamcorper elit diam justo. Elitr vel in invidunt in esse tincidunt et dolore nulla lorem vero laoreet vero kasd aliquyam gubergren sanctus feugiat. Est amet et aliquip aliquyam lobortis dolore esse dolor duo lorem rebum. Dolor sed magna magna sit erat erat et. No voluptua diam aliquyam dolor dolores lorem sed labore. Sit eros lorem dolor cum ut takimata est accusam diam dolore labore. Aliquyam et tempor dolor erat blandit at eos invidunt. Est et sanctus justo sit blandit labore consequat et duo. Rebum amet vulputate magna dolor et in feugait tation diam tempor ullamcorper. Diam in rebum tempor odio sed sea eos ipsum dolore vero diam consequat. Sanctus ipsum ullamcorper accusam stet et et at sed aliquam et ea. Dolore et at dolore labore te et kasd consetetur. Rebum magna et facilisis. Et stet et lorem amet ut tempor takimata dolor vero sea vel dolores sit. No accusam est ad elitr erat accusam eirmod vero dolores velit sed adipiscing.",
                foodItemPhotoUrl = "https://cc",
                basePrice = 28000,
                variations = listOf()
            ), {},
            Modifier
                .padding(16.dp)
                .width(400.dp),
            inCartCount = 3
        )
    }
}