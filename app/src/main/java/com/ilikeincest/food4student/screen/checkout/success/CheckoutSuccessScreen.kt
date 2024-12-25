package com.ilikeincest.food4student.screen.checkout.success

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CheckoutSuccessScreen(
    onNavigateToMain: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxHeight().padding(24.dp)
    ) {
        var progress by remember { mutableFloatStateOf(0f) }
        val animatedProgress by
        animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )

        LaunchedEffect(Unit) {
            while (true) {
                progress = 0f
                delay(2000)
                progress = 1f
                delay(2000)
            }
        }

        Column {
            Text("Đặt hàng thành công", style = typography.displaySmall)
            Text("Bạn chờ tí, shop xác nhận đơn nha", style = typography.bodyLarge)
        }
        LinearWavyProgressIndicator(
            progress = { animatedProgress },
            color = colorScheme.tertiary,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onNavigateToMain) {
            Text("Về trang chủ", fontSize = 18.sp)
        }
    }
}

@Preview
@Composable
private fun P() {
    CheckoutSuccessScreen {  }
}