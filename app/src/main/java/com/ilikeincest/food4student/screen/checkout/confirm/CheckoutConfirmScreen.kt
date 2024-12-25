package com.ilikeincest.food4student.screen.checkout.confirm

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.component.LoadingDialog
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.SavedShippingLocation
import com.ilikeincest.food4student.model.SavedShippingLocationType
import com.ilikeincest.food4student.screen.checkout.confirm.component.CheckoutShippingLocationCard
import com.ilikeincest.food4student.screen.checkout.confirm.component.PreviewOrderCard
import com.ilikeincest.food4student.screen.restaurant.detail.Cart
import com.ilikeincest.food4student.screen.restaurant.detail.CartItem

@Composable
fun CheckoutConfirmScreen(
    order: Cart,
    onSuccess: () -> Unit,
    onNavigateToShippingLocation: () -> Unit,
    vm: CheckoutConfirmViewModel = hiltViewModel()
) {
    // TODO reject if no selected shipping location
    val shippingLocation by vm.shippingLocation.collectAsState()
    val context = LocalContext.current
    val isLoading by vm.isLoading
    var error by vm.error

    LaunchedEffect(Unit) {
        vm.registerLocationFromDStore(context)
        vm.initUserInfo(context)
    }

    LoadingDialog("Đang tải", isLoading)
    if (error.isNotEmpty()) {
        ErrorDialog(
            message = error,
            onDismiss = { error = "" },
            title = "Lỗi đặt hàng"
        )
    }

    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
        ) {
            Spacer(Modifier.height(200.dp))
            Text("Kiểm tra lại đơn...",
                style = typography.displaySmall
            )
            CheckoutShippingLocationCard(onClick = onNavigateToShippingLocation,
                currentLocation = shippingLocation
            )

            PreviewOrderCard(
                restaurantName = order.restaurantName,
                shopImageUrl = order.shopImageUrl,
                orderItems = order.cartItems,
                modifier = Modifier.fillMaxWidth()
            )

            val shippingSelected by remember { derivedStateOf { shippingLocation != null } }
            Button(onClick = { vm.commitOrder(order, onSuccess) },
                enabled = shippingSelected,
                modifier = Modifier.align(Alignment.End).height(54.dp)
            ) {
                Text("Let's eat!", fontSize = 18.sp)
            }

            Spacer(Modifier.height(200.dp))
        }
    }
}