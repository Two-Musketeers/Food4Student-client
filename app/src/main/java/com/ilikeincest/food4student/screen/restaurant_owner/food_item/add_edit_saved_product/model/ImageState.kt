package com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_edit_saved_product.model

import android.net.Uri

data class ImageState(
    val imageUrl: String? = null, // URL from server
    val imageUri: Uri? = null      // Local URI selected by the user
) {
    val isImageSelected: Boolean
        get() = imageUri != null || imageUrl != null
}
