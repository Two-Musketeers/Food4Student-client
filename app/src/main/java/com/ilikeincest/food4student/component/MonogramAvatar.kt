package com.ilikeincest.food4student.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun AsyncImageOrMonogram(
    model: Any?,
    contentDescription: String?,
    name: String,
    size: Dp = 28.dp,
    textStyle: TextStyle = typography.bodySmall,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val fallbackMonogram = monogramAvatarPainter(
        name = name,
        size = size,
        textStyle = textStyle
    )
    AsyncImage(
        model = model,
        placeholder = fallbackMonogram, // TODO
        fallback = fallbackMonogram,
        error = fallbackMonogram,
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    )
}

/**
 * Create a monogram avatar [Painter] with the given name.
 *
 * @param name Name of "people" to represent.
 * The actual content inside monogram will be generated from this
 */
@Composable
fun monogramAvatarPainter(
    name: String,
    size: Dp = 28.dp,
    backgroundColor: Color = colorScheme.primaryContainer,
    textColor: Color = colorScheme.onPrimaryContainer,
    textStyle: TextStyle = typography.bodySmall,
) : Painter {
    var sizeFloat: Float
    with(LocalDensity.current) {
        sizeFloat = size.toPx()
    }
    val intrinsicSize = Size(sizeFloat, sizeFloat)
    return MonogramAvatarPainter(
        initials = getMonogram(name),
        textMeasurer = rememberTextMeasurer(),
        backgroundColor = backgroundColor,
        textColor = textColor,
        textStyle = textStyle,
        intrinsicSize = intrinsicSize
    )
}

private class MonogramAvatarPainter(
    private val initials: String,
    private val textMeasurer: TextMeasurer,
    private val backgroundColor: Color,
    private val textColor: Color,
    private val textStyle: TextStyle,
    override val intrinsicSize: Size
) : Painter() {
    override fun DrawScope.onDraw() {
        val textLayoutResult = textMeasurer.measure(
            text = initials,
            style = textStyle,
            constraints = Constraints(
                minWidth = 0,
                maxWidth = intrinsicSize.width.toInt(),
                minHeight = 0,
                maxHeight = intrinsicSize.height.toInt()
            )
        )
        drawIntoCanvas {
            drawCircle(SolidColor(backgroundColor))
            drawText(
                textLayoutResult = textLayoutResult,
                brush = SolidColor(textColor),
                topLeft = Offset(
                    x = center.x - textLayoutResult.size.width / 2,
                    y = center.y - textLayoutResult.size.height / 2,
                ),
            )
        }
    }
}

private fun getMonogram(name: String): String {
    return name
        .split("\\s+".toRegex())
        .take(2)
        .map { if (it.isNotBlank()) it.first().uppercaseChar() else "" }
        .joinToString("")
}