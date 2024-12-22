package com.ilikeincest.food4student.screen.restaurant.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RatingChip(
    starRating: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.tertiaryContainer,
            contentColor = colorScheme.onTertiaryContainer
        ),
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
        ) {
            Spacer(Modifier.width(10.dp))
            Text(
                starRating,
                style = typography.bodyLarge,
            )
            Icon(
                Icons.Rounded.Star, null,
                Modifier.size(22.dp).offset(y = (-1).dp)
            )
            Icon(
                Icons.Default.ChevronRight, null,
                modifier = Modifier.offset(x = (-4).dp)
            )
        }
    }
}