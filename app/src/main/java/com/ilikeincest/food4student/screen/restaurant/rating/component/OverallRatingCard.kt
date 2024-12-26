package com.ilikeincest.food4student.screen.restaurant.rating.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview
import com.materialkolor.ktx.harmonize

@Composable
fun OverallRatingCard(
    totalRatings: Int,
    averageRating: Double,
    perStarRatings: List<Int>,
    modifier: Modifier = Modifier
) {
    val starColor = Color(0xFFf2ca00).harmonize(colorScheme.onSurface)
    Card(
        shape = shapes.extraLarge,
        colors = CardDefaults.cardColors().copy(
            containerColor = colorScheme.surfaceContainer
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal =  24.dp)
                .padding(top = 24.dp, bottom = 16.dp)
        ) {
            val ratingToShow = if (totalRatings == 0) "---"
                else String.format("%.2f", averageRating)
            Text(ratingToShow,
                style = typography.titleLarge.copy(
                    fontWeight = FontWeight.W700,
                    fontSize = 24.sp
                )
            )
            Icon(Icons.Rounded.Star, null, tint = starColor,
                modifier = Modifier.size(28.dp).padding(start = 4.dp)
            )
            HorizontalDivider(Modifier.weight(1f).padding(horizontal = 16.dp))
            Text("$totalRatings lượt đánh giá",
                style = typography.bodyMedium
            )
        }
        HorizontalDivider(
            thickness = 2.dp,
            color = colorScheme.surface
        )
        Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
            for (i in 5 downTo 1) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(i.toString(), style = typography.bodyMedium)
                    Spacer(Modifier.width(12.dp))
                    LinearProgressIndicator(
                        progress = {
                            if (perStarRatings.size < i || totalRatings == 0)
                                return@LinearProgressIndicator 0f
                            perStarRatings[i-1].toFloat() / totalRatings
                        },
                        color = starColor,
                        trackColor = colorScheme.surfaceContainerHighest,
                        modifier = Modifier.height(6.dp).fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Prev() { ComponentPreview {
    OverallRatingCard(
        totalRatings = 874,
        averageRating = 4.3,
        perStarRatings = listOf(2, 12, 32, 132, 696),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
} }