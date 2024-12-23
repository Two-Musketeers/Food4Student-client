package com.ilikeincest.food4student.screen.restaurant.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview

@Composable
fun TonalFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val default = IconButtonDefaults.filledTonalIconButtonColors()
    val containerColor = default.containerColor
    val contentColor = default.contentColor
    val selectedContainerColor = colorScheme.error
    val selectedContentColor = colorScheme.onError

    val actualContainer by animateColorAsState(
        if (isFavorite) selectedContainerColor else containerColor,
        label = "favorite button container",
    )
    val actualContent by animateColorAsState(
        if (isFavorite) selectedContentColor else contentColor,
        label = "favorite button content"
    )

    FilledTonalIconButton(
        onClick = onClick,
        colors = default.copy(
            containerColor = actualContainer,
            contentColor = actualContent
        ),
        modifier = modifier
    ) {
        Crossfade(
            targetState = isFavorite,
            animationSpec = spring()
        ) {
            if (it) {
                Icon(Icons.Rounded.Favorite, null)
            } else {
                Icon(Icons.Rounded.FavoriteBorder,null)
            }
        }
        Icon(Icons.Default.FavoriteBorder, null)
    }
}

@Preview
@Composable
private fun Prev() {
    ComponentPreview {
        var isFavorite by remember { mutableStateOf(false) }

        TonalFavoriteButton(
            isFavorite,
            { isFavorite = !isFavorite }
        )
    }
}