package com.ilikeincest.food4student.screen.auth.select_role.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview

@Composable
fun RoleCard(
    selected: Boolean,
    onSelect: () -> Unit,
    title: String,
    description: String,
    @DrawableRes unselectedIcon: Int,
    @DrawableRes selectedIcon: Int,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        if (selected) colorScheme.primary else colorScheme.surfaceContainer,
        label = "card bg color"
    )
    val contentColor by animateColorAsState(
        if (selected) colorScheme.onPrimary else colorScheme.onSurface,
        label = "card content color"
    )
    val border by animateDpAsState(
        if (selected) (-1).dp else 1.dp, // this is dumb holy shit
        label = "card border"
    )
    val padding by animateDpAsState(
        if (selected) 0.dp else 2.dp,
        label = "card padding"
    )
    val elevation by animateDpAsState(
        if(selected) 8.dp else 0.dp,
        label = "elevation"
    )
    Card(
        border = BorderStroke(border, colorScheme.outline),
        elevation = CardDefaults.cardElevation(elevation),
        colors = CardDefaults.cardColors().copy(
            containerColor = backgroundColor,
            contentColor = contentColor,
        ),
        onClick = onSelect,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = padding),
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text(title, style = typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text(description, style = typography.bodyLarge)
                }
                Spacer(Modifier.width(16.dp))
                val icon = if (selected) selectedIcon else unselectedIcon
                Icon(painterResource(icon), null, Modifier.size(48.dp))
            }
            AnimatedVisibility(selected) {
                Spacer(Modifier.height(8.dp))
                Button(onClick = onSelect,
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = colorScheme.primaryContainer,
                        contentColor = colorScheme.onPrimaryContainer
                    ),
                    contentPadding = PaddingValues(start = 24.dp, top = 10.dp, end = 16.dp, bottom = 10.dp)
                ) {
                    Text("Tiếp tục", style = typography.titleMedium)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                }
            }
        }
    }
}

@Preview
@Composable
private fun Prev() { ComponentPreview {
    var selected by rememberSaveable { mutableStateOf(true) }

    RoleCard(
        selected = selected,
        onSelect = { selected = !selected },
        title = "Thực khách",
        description = "Đặt và thưởng thức các món ăn đa dạng, ngay từ đầu ngón tay.",
        selectedIcon = R.drawable.ramen_dining_filled,
        unselectedIcon = R.drawable.ramen_dining,
        modifier = Modifier.padding(32.dp)
    )
} }