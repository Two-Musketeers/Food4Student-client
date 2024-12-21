package com.ilikeincest.food4student.screen.auth.select_role

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.component.DividerWithSubhead
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.component.LoadingDialog
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
import com.ilikeincest.food4student.model.Location

@Composable
fun SelectRoleRestaurantScreen(
    selectedLocation: Location?,
    onNavigateToLocationPicker: () -> Unit,
    vm: SelectRoleRestaurantViewModel = hiltViewModel()
) {
    var name by vm.name
    var description by vm.description
    var phoneNumber by vm.phoneNumber
    var ownerPhoneNumber by vm.ownerPhoneNumber
    var address by vm.address
    var latitude by vm.latitude
    var longitude by vm.longitude
    LaunchedEffect(selectedLocation) {
        if (selectedLocation == null) return@LaunchedEffect
        latitude = selectedLocation.latitude
        longitude = selectedLocation.longitude
        address = selectedLocation.address
    }
    val error by vm.error.collectAsState()
    val showLoading by vm.showLoading.collectAsState()
    if (error.isNotEmpty()) {
        ErrorDialog(
            message = error,
            onDismiss = { vm.dismissError() },
        )
    }
    LoadingDialog(
        label = "Bạn chờ tí nha...",
        isVisible = showLoading
    )
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
        ) {
            Spacer(Modifier.height(200.dp))
            Text("Đăng ký\nnhà hàng", style = typography.displayMedium)
            Spacer(Modifier.height(32.dp))
            // name
            TextField(
                value = name, onValueChange = { name = it },
                label = { Text("Tên nhà hàng") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(32.dp))
            // desc box
            val descMaxChars = 200
            TextField(
                value = description, onValueChange = {
                    if (it.length <= descMaxChars)
                        description = it
                },
                label = { Text("Mô tả nhà hàng") },
                singleLine = false,
                supportingText = {
                    Text(
                        text = "${description.length} / $descMaxChars",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )
            Spacer(Modifier.height(16.dp))
            // phone
            TextField(
                value = phoneNumber, onValueChange = { phoneNumber = it },
                label = { Text("Số điện thoại nhà hàng") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                supportingText = {
                    Text("Khách hàng sẽ liên lạc bạn qua số này")
                },
                modifier = Modifier.fillMaxWidth()
            )
            // owner number
            Spacer(Modifier.height(24.dp))
            TextField(
                value = ownerPhoneNumber, onValueChange = { ownerPhoneNumber = it },
                label = { Text("Số điện thoại cá nhân") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                ),
                supportingText = {
                    Text("Food4Student sẽ liên lạc bạn qua số này")
                },
                modifier = Modifier.fillMaxWidth()
            )
            // address
            Spacer(Modifier.height(24.dp))
            DividerWithSubhead(subhead = { Text("Thông tin vị trí") })
            Spacer(Modifier.height(16.dp))
            FilledTonalButton(
                onClick = onNavigateToLocationPicker,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Chọn từ bản đồ")
            }
            if (latitude != Double.POSITIVE_INFINITY) {
                Spacer(Modifier.height(16.dp))
                TextField(
                    value = address, onValueChange = { address = it },
                    label = { Text("Địa chỉ") },
                    singleLine = true,
                    supportingText = {
                        Text("Chỉnh sửa địa chỉ từ bản đồ nếu chưa chính xác")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                TextField(
                    value = latitude.toString(), onValueChange = {},
                    label = { Text("Latitude") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                TextField(
                    value = longitude.toString(), onValueChange = {},
                    label = { Text("Longitude") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // TODO: show map to visualize where the location is
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    vm.registerRestaurant(onSuccess = {
                        // TODO

                    })
                },
                enabled = latitude != Double.POSITIVE_INFINITY,
                contentPadding = PaddingValues(28.dp, 12.dp, 18.dp, 12.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Let's eat!", style = typography.titleMedium)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
            }
            Spacer(Modifier.height(200.dp))
        }
    }
}

@Preview
@Composable
private fun Prev() { ScreenPreview {
    SelectRoleRestaurantScreen(null, {})
} }