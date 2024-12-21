package com.ilikeincest.food4student.screen.auth.select_role

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.screen.auth.select_role.component.RoleCard

@Composable
fun SelectRoleScreen(
    onSignUpAsUser: () -> Unit,
    onSignUpAsRestaurant: () -> Unit
) {
    Surface { Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
    ) {
        Text("Bạn là...",
            style = typography.displayMedium,
            modifier = Modifier.padding(vertical = 42.dp)
        )

        Column(Modifier.defaultMinSize(minHeight = 440.dp)) {
            var selected by rememberSaveable { mutableIntStateOf(0) }
            RoleCard(
                selected = selected == 1,
                onSelect = {
                    if (selected != 1) selected = 1
                    else onSignUpAsUser()
                },
                title = "Thực khách",
                description = "Đặt và thưởng thức các món ăn đa dạng, ngay từ đầu ngón tay.",
                selectedIcon = R.drawable.ramen_dining_filled,
                unselectedIcon = R.drawable.ramen_dining,
            )
            Spacer(Modifier.height(42.dp))
            RoleCard(
                selected = selected == 2,
                onSelect = {
                    if (selected != 2) selected = 2
                    else onSignUpAsRestaurant()
                },
                title = "Nhà hàng",
                description = "Quảng bá, kết nối và quản lý đơn hàng với cộng đồng thực khách sành ăn.",
                selectedIcon = R.drawable.storefront_filled,
                unselectedIcon = R.drawable.storefront,
            )
        }
    } }
}

@Preview
@Composable
private fun Prev() { ScreenPreview {
    SelectRoleScreen({}, {})
} }