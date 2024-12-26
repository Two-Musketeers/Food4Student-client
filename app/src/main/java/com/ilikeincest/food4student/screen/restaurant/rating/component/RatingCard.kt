package com.ilikeincest.food4student.screen.restaurant.rating.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview
import com.ilikeincest.food4student.dto.RatingDto
import com.materialkolor.ktx.harmonize

@Composable
fun RatingCard(
    it: RatingDto,
    modifier: Modifier = Modifier
) {
    if (it.comment == null) return
    val starColor = Color(0xFFf2ca00).harmonize(colorScheme.onSurface)
    var showMore by remember { mutableStateOf(false) }
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            colorScheme.surface
        ),
        onClick = { showMore = !showMore },
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = modifier
    ) {
        Box(Modifier.fillMaxWidth()) {
            Icon(painterResource(R.drawable.quarter_circle), null,
                tint = colorScheme.secondaryContainer.copy(alpha = 0.3f),
                modifier = Modifier.align(Alignment.BottomEnd).size(100.dp)
            )
            Column(Modifier.padding(16.dp)
                .requiredHeightIn(min = 80.dp)
                .height(IntrinsicSize.Min)
            ) {
                AnimatedContent(showMore,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "view more desc"
                ) { showMore ->
                    Text(it.comment,
                        maxLines = if (showMore) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(16.dp))
                Spacer(Modifier.weight(1f))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (i in 1..it.stars) {
                        Icon(Icons.Default.Star, null, tint = starColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Prev() { ComponentPreview {
    RatingCard(
        RatingDto(
            id = "",
            stars = 4,
            comment = "Consetetur aliquyam voluptua et tempor sit. Et in aliquyam sanctus dolores tincidunt tempor invidunt nobis vel ipsum justo kasd. Mazim"
        ),
        Modifier
            .background(colorScheme.secondaryContainer.copy(alpha = 0.4f))
            .fillMaxWidth()
            .padding(16.dp)
    )
} }