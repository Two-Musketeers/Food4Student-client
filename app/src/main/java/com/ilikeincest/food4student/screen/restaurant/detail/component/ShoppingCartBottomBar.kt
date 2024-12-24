package com.ilikeincest.food4student.screen.restaurant.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilikeincest.food4student.screen.restaurant.detail.RestaurantDetailViewModel
import com.ilikeincest.food4student.util.formatPrice

@Composable
fun ShoppingCartBottomBar(
    viewModel: RestaurantDetailViewModel,
    onCartClick: () -> Unit
) {
    val totalPrice by viewModel.totalPrice.collectAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onCartClick() }
            .background(colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "View Cart",
            tint = colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp).size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Hóa đơn: ${formatPrice(totalPrice)}",
            style = typography.titleLarge.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            color = colorScheme.onPrimaryContainer
        )
    }
}
