package com.ilikeincest.food4student.screen.food_item.add_edit_saved_product.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ilikeincest.food4student.screen.food_item.add_edit_saved_product.model.ImageState

@Composable
fun ImagePickerField(
    imageState: ImageState,
    onImageClick: () -> Unit,
    onDeleteImage: () -> Unit,
    modifier: Modifier
) {
    Row {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(colorScheme.surfaceVariant)
                .clickable { onImageClick() }
        ) {
            if (imageState.isImageSelected) {
                // Determine the image source: URI or URL
                val imageModel = imageState.imageUri ?: imageState.imageUrl

                // Display the image using Coil's AsyncImage
                AsyncImage(
                    model = imageModel,
                    contentDescription = "Food item photo",
                    placeholder = ColorPainter(colorScheme.primaryContainer),
                    error = ColorPainter(colorScheme.error),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                )
            } else {
                // Placeholder UI when no image is selected
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Image",
                        tint = colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Thêm ảnh",
                        color = colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        // 'X' button to delete the image
        if (imageState.isImageSelected) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .offset(x = (-12).dp, y = (-8).dp)
                    .size(20.dp) // Adjust the size of the entire button here
                    .background(
                        color = colorScheme.inverseSurface.copy(alpha = 0.7f),
                        shape = CircleShape
                    )
                    .clickable { onDeleteImage() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete Image",
                    tint = colorScheme.inverseOnSurface,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ImagePickerFieldPreview() {
    ImagePickerField(
        imageState = ImageState(),
        onImageClick = {},
        onDeleteImage = {},
        modifier = Modifier.size(125.dp)
    )
}