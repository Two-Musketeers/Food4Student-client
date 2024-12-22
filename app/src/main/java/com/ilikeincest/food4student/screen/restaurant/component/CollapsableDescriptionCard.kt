package com.ilikeincest.food4student.screen.restaurant.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CollapsableDescriptionCard(
    description: String,
    modifier: Modifier = Modifier,
    minimizedMaxLines: Int = 3,
) {
    var viewMore by rememberSaveable { mutableStateOf(false) }
    Card(onClick = { viewMore = !viewMore }, modifier = modifier.fillMaxWidth()) {
        AnimatedContent(viewMore,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "view more desc"
        ) { showMore ->
            Text(description, style = typography.bodyLarge,
                maxLines = if (showMore) Int.MAX_VALUE else minimizedMaxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}