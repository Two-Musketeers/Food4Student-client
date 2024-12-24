package com.ilikeincest.food4student.screen.restaurant_owner.component

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
import com.ilikeincest.food4student.screen.restaurant.detail.component.CollapsableDescriptionCard
import com.ilikeincest.food4student.screen.restaurant.detail.component.RatingChip
import com.ilikeincest.food4student.screen.restaurant.detail.component.TonalFavoriteButton

@Composable
fun RestaurantOwnerHeader(
    name: String,
    starRating: String,
    description: String?,
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
            RatingChip(starRating, onClick = {}) // TODO

            Spacer(Modifier.weight(1f))
        }

        // Desc
        Spacer(Modifier.height(8.dp))
        CollapsableDescriptionCard(description)
    }
}