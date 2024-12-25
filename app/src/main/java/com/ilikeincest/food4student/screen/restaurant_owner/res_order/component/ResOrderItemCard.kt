package com.ilikeincest.food4student.screen.restaurant_owner.res_order.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview
import com.ilikeincest.food4student.util.formatPrice

@Composable
fun ResOrderItemCard(
    imageModel: Any?,
    title: String,
    notes: String?,
    price: Int,
    quantity: Int,
    modifier: Modifier = Modifier
) {
    val itemHeight = 92.dp
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        AsyncImage(
            model = imageModel, contentDescription = "Food image",
            placeholder = ColorPainter(colorScheme.secondaryContainer),
            error = ColorPainter(colorScheme.error),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(itemHeight)
                .clip(RoundedCornerShape(16.dp))
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Text(
                    title, style = typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                if (!notes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        notes, style = typography.bodySmall,
                        color = colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val priceFormatted = formatPrice(price)
                Text(priceFormatted, style = typography.titleMedium)
                Text("x$quantity", style = typography.bodySmall)
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun OrderItemPreview() {
    ComponentPreview {
        ResOrderItemCard(
            imageModel = R.drawable.ic_launcher_background,
            title = "Trà sữa Phô mai tươi 123123213123213123",
            notes = "Size S - Không đá",
            price = 56000,
            quantity = 2,
            modifier = Modifier.width(300.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun OrderItemPreview2() {
    ComponentPreview {
        ResOrderItemCard(
            imageModel = R.drawable.ic_launcher_background,
            title = "Trà sữa Phô mai tươi 123123213123213123",
            notes = "Size S - Không đá\nSize S - Không đá\nSize S - Không đá\nSize S - Không đá\nSize S - Không đá\nSize S - Không đá\n",
            price = 56000,
            quantity = 2,
            modifier = Modifier.width(300.dp),
        )
    }
}