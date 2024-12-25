package com.ilikeincest.food4student.screen.restaurant_owner.account_center

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.DividerWithSubhead
import com.ilikeincest.food4student.model.Location
import com.ilikeincest.food4student.screen.restaurant_owner.RestaurantOwnerViewModel
import com.ilikeincest.food4student.screen.restaurant_owner.food_item.add_edit_saved_product.component.ImagePickerField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantAccountCenterScreen(
    selectedLocation: Location?,
    modifier: Modifier = Modifier,
    viewModel: RestaurantOwnerViewModel,
    onNavigateToLocationPicker: () -> Unit
) {

    val restaurantState = viewModel.restaurant.collectAsState()
    val restaurant = restaurantState.value
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val context = LocalContext.current

    val logoImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.setLogoImage(it)
        }
    }

    LaunchedEffect(selectedLocation) {
        if (selectedLocation == null) return@LaunchedEffect
        viewModel.updateRestaurantProperty("latitude", selectedLocation.latitude.toString())
        viewModel.updateRestaurantProperty("longitude", selectedLocation.longitude.toString())
        viewModel.updateRestaurantProperty("address", selectedLocation.address)
    }

    val bannerImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.setBannerImage(it)
        }
    }

    if (errorMessage.isNotEmpty()) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { viewModel.dismissMessages() }
        )
    }

    if (successMessage.isNotEmpty()) {
        SuccessDialog(
            message = successMessage,
            onDismiss = { viewModel.dismissMessages() }
        )
    }
    restaurant?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Chi tiết nhà hàng") },
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo Picker
                    ImagePickerField(
                        imageState = viewModel.logoImageState.collectAsState().value,
                        onImageClick = { logoImagePickerLauncher.launch("image/*") },
                        onDeleteImage = { viewModel.deleteLogoImage() },
                        modifier = Modifier
                            .sizeIn(minWidth = 125.dp, minHeight = 125.dp)
                            .size(150.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        title = "Thêm ảnh nền"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Banner Picker with 1:2 ratio
                    ImagePickerField(
                        imageState = viewModel.bannerImageState.collectAsState().value,
                        onImageClick = { bannerImagePickerLauncher.launch("image/*") },
                        onDeleteImage = { viewModel.deleteBannerImage() },
                        modifier = Modifier
                            .sizeIn(minWidth = 125.dp, minHeight = 125.dp)
                            .aspectRatio(2f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        title = "Thêm ảnh bìa"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    EditableCard(
                        title = "Restaurant Name",
                        value = restaurant.name,
                        icon = Icons.Filled.Edit
                    ) { newValue ->
                        viewModel.updateRestaurantProperty("name", newValue)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    EditableCard(
                        title = "Description",
                        value = restaurant.description ?: "",
                        icon = Icons.Filled.Edit
                    ) { newValue ->
                        viewModel.updateRestaurantProperty("description", newValue)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    EditableCard(
                        title = "Phone Number",
                        value = restaurant.phoneNumber, // Ensure this is editable
                        icon = Icons.Filled.Edit
                    ) { newValue ->
                        viewModel.updateRestaurantProperty("phoneNumber", newValue)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    DividerWithSubhead(subhead = { Text("Thông tin vị trí") })
                    Spacer(Modifier.height(16.dp))
                    FilledTonalButton(
                        onClick = onNavigateToLocationPicker,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Chọn từ bản đồ")
                    }
                    Spacer(Modifier.height(16.dp))
                    TextField(
                        value = restaurant.address, onValueChange = { viewModel.updateAddress(it) },
                        label = { Text("Địa chỉ") },
                        singleLine = true,
                        supportingText = {
                            Text("Chỉnh sửa địa chỉ từ bản đồ nếu chưa chính xác")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                    TextField(
                        value = restaurant.latitude.toString(), onValueChange = {},
                        label = { Text("Latitude") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                    TextField(
                        value = restaurant.longitude.toString(), onValueChange = {},
                        label = { Text("Longitude") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.saveChanges() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.hasChanges()
                    ) {
                        Text("Save All Changes")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.onSignOutClick(context) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign Out")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.onDeleteAccountClick(context) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.error)
                    ) {
                        Text("Delete Account")
                    }
                }
            }
        )
    }
}

@Composable
fun EditableCard(
    title: String,
    value: String,
    icon: ImageVector,
    onEditClick: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        onClick = { showDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 80.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = title, style = typography.titleSmall)
                Text(text = value, style = typography.bodyMedium)
            }
            IconButton(onClick = { showDialog = true }) {
                Icon(imageVector = icon, contentDescription = "Edit $title")
            }
        }
    }

    if (showDialog) {
        EditPropertyDialog(
            title = "Edit $title",
            currentValue = value,
            onDismiss = { showDialog = false },
            onConfirm = { updatedValue ->
                onEditClick(updatedValue)
                showDialog = false
            }
        )
    }
}

@Composable
fun EditPropertyDialog(
    title: String,
    currentValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var input by remember { mutableStateOf(currentValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            TextField(
                value = input,
                onValueChange = { input = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(input) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ErrorDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
fun SuccessDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Success") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}