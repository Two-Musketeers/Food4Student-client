package com.ilikeincest.food4student.screen.main_page.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview

/**
 * @param isFavorite Set this to null to hide the favorite icon
 */
@Composable
fun ShopListingCard(
    shopName: String,
    starRating: String,
    distance: String,
    timeAway: String,
    shopImageModel: Any?,
    isFavorite: Boolean?,
    onFavoriteChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val height = 92.dp
    Row(Modifier
        .clickable { onClick() }
        .then(modifier)
        .height(height)
    ) {
        // TODO: refactor this to global component, and share with order item card
        AsyncImage(
            model = shopImageModel,
            contentDescription = "Shop image",
            placeholder = ColorPainter(colorScheme.primaryContainer),
            error = ColorPainter(colorScheme.error),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(height)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(Modifier.width(16.dp))
        Box(Modifier.height(92.dp).fillMaxWidth()) {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    shopName,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.titleMedium
                )
                Row(Modifier.fillMaxWidth().wrapContentHeight()) {
                    Text(
                        starRating,
                        style = typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                    Icon(Icons.Rounded.Star, null, Modifier.size(14.dp))
                    VerticalDivider(Modifier.padding(horizontal = 8.dp).height(16.dp))
                    Text(
                        distance,
                        style = typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                    VerticalDivider(Modifier.padding(horizontal = 8.dp).height(16.dp))
                    Text(
                        timeAway,
                        style = typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }
            if (isFavorite != null) {
                IconToggleButton(
                    checked = isFavorite,
                    onCheckedChange = onFavoriteChange,
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    val icon = if (isFavorite) Icons.Rounded.Favorite
                        else Icons.Rounded.FavoriteBorder
                    Icon(icon, null)
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun FavoriteCardPrev() { ComponentPreview {
    ShopListingCard(
        "Phúc Long",
        "4.2",
        "2.5km",
        "42 phút",
        null,
        true,
        {},
        {},
        Modifier.padding(16.dp).width(340.dp)
    )
} }

@PreviewLightDark
@Composable
private fun LongAssName() { ComponentPreview {
    ShopListingCard(
        "Phúc Long Coop Test tiêu đề siêu dài holy shit nó còn dài hơn",
        "4.2",
        "2.5km",
        "42 phút",
        null,
        true,
        {},
        {},
        Modifier.padding(16.dp).width(340.dp)
    )
} }