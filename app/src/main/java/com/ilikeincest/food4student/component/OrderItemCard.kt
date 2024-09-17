package com.ilikeincest.food4student.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderItemCard(
    image: Painter,
    title: String,
    notes: String,
    price: Int,
    quantity: Int,
    modifier: Modifier = Modifier
) {
    val itemHeight = 92.dp
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
        modifier = modifier.height(itemHeight)
    ) {
        Image(
            painter = image, contentDescription = "Food image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(itemHeight)
                .width(itemHeight)
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    notes, style = typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("x$quantity", style = typography.bodySmall)
                val locale = Locale.forLanguageTag("vi-VN") // TODO: hard code for now.
                val priceFormatted = NumberFormat.getIntegerInstance(locale).format(price)
                val currencySymbol = NumberFormat.getCurrencyInstance(locale).currency?.symbol
                Text("$priceFormatted$currencySymbol", style = typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderItemPreview() {
    OrderItemCard(
        image = painterResource(id = R.drawable.ic_launcher_background),
        title = "Trà sữa Phô mai tươi 123123213123213123",
        notes = "Size S - Không đá",
        price = 56000,
        quantity = 2,
        modifier = Modifier.width(300.dp),
    )
}