package com.ilikeincest.food4student.screen.restaurant.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RestaurantHeader(
    name: String,
    starRating: String,
    distance: String,
    timeAway: String,
    description: String?,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onNavigateToRating: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp).padding(top = 8.dp)
    ) {
        Text(name, style = typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold
        ))

        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RatingChip(starRating, onClick = onNavigateToRating)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                Spacer(Modifier.width(16.dp))
                Text(
                    distance,
                    style = typography.bodyLarge,
                    color = colorScheme.onSurfaceVariant
                )
                VerticalDivider(Modifier.padding(horizontal = 16.dp))
                Text(
                    timeAway,
                    style = typography.bodyLarge,
                    color = colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.weight(1f))

            TonalFavoriteButton(isFavorite, onClick = onFavoriteToggle)
        }

        // Desc
        Spacer(Modifier.height(8.dp))
        CollapsableDescriptionCard(description)
    }
}