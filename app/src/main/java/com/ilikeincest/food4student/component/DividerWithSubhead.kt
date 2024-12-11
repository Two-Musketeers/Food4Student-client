package com.ilikeincest.food4student.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview

@Composable
fun DividerWithSubhead(
    subhead: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val textStyle = typography.titleSmall
    val contentColor = colorScheme.onSurfaceVariant
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        val mergedStyle = LocalTextStyle.current.merge(textStyle)
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides mergedStyle,
        ) {
            subhead()
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
private fun Prev() { ComponentPreview {
    DividerWithSubhead(
        subhead = { Text("Subhead for your part") },
        modifier = Modifier.width(300.dp)
    )
} }