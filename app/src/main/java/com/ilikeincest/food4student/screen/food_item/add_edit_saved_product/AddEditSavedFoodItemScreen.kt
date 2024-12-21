package com.ilikeincest.food4student.screen.food_item.add_edit_saved_product

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.ConfirmDiscardUnsavedDialog
import com.ilikeincest.food4student.component.DividerWithSubhead
import com.ilikeincest.food4student.model.FoodItem
import com.ilikeincest.food4student.screen.food_item.ConfirmDeleteDialog
import com.ilikeincest.food4student.screen.food_item.add_edit_saved_product.component.AddEditSavedTopBarProduct
import com.ilikeincest.food4student.screen.food_item.add_edit_saved_product.component.CategoryAndVariationField
import com.ilikeincest.food4student.screen.food_item.add_edit_saved_product.component.ImagePickerField
import com.ilikeincest.food4student.screen.food_item.add_edit_saved_product.component.PriceBottomSheet
import com.ilikeincest.food4student.screen.food_item.add_edit_saved_product.model.ImageState
import com.ilikeincest.food4student.screen.restaurant.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSavedFoodItemScreen(
    onNavigateUp: () -> Unit,
    onNavigateToFoodCategory: () -> Unit,
    onNavigateToVariation: () -> Unit,
    viewModel: RestaurantViewModel
) {
    val foodItem = viewModel.selectedFoodItem.collectAsState().value

    val name by viewModel.foodName.collectAsState()
    val description by viewModel.foodDescription.collectAsState()
    val basePrice by viewModel.foodBasePrice.collectAsState()
    var tempPrice by remember { mutableStateOf(basePrice) }

    val selectedCategory = viewModel.selectedFoodCategory.collectAsState().value
    val isEditScreen = foodItem != null
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var currentCategory by remember { mutableStateOf("") }

    val unsavedVariations by viewModel.unsavedVariations.collectAsState()
    val unsavedImageUri by viewModel.unsavedImageUri.collectAsState()

    // Sync the category display text with selectedCategory
    LaunchedEffect(selectedCategory) {
        currentCategory = selectedCategory?.name ?: ""
    }

    var showConfirmDiscardDialog by remember { mutableStateOf(false) }
    val valueChanged = true
    val onNavUp = {
        if (valueChanged) {
            showConfirmDiscardDialog = true
        } else {
            onNavigateUp()
        }
    }

    BackHandler(enabled = valueChanged) { showConfirmDiscardDialog = true }

    if (showConfirmDiscardDialog) {
        ConfirmDiscardUnsavedDialog(
            onConfirm = onNavigateUp,
            onDismiss = { showConfirmDiscardDialog = false }
        )
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        PriceBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            price = tempPrice,
            onPriceChange = { tempPrice = it },
            onSavePrice = {
                viewModel.setFoodBasePrice(tempPrice)
                showBottomSheet = false
            }
        )
    }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setUnsavedImage(uri)
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            title = "Xác nhận xoá",
            text = "Bạn có chắc chắn muốn xoá món ăn này?",
            onConfirm = {
                viewModel.removeFoodItem(foodItem?.id ?: "", selectedCategory?.id ?: "")
                onNavigateUp()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Scaffold(
        topBar = {
            AddEditSavedTopBarProduct(
                isEdit = isEditScreen,
                onNavigateUp = { onNavUp() },
                onDelete = {
                    showDeleteDialog = true
                },
                onSave = {
                    val foodItemCreateDto = FoodItem(
                        id = if (isEditScreen) foodItem?.id ?: "" else "",
                        name = name,
                        description = description,
                        basePrice = basePrice.toIntOrNull() ?: 0,
                        foodItemPhotoUrl = unsavedImageUri.toString(),
                        variations = unsavedVariations
                    )
                    val categoryId = selectedCategory?.id ?: ""
                    viewModel.saveFoodItem(foodItemCreateDto, foodCategoryId = categoryId)
                    onNavigateUp()
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.imePadding()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            DividerWithSubhead(subhead = { Text("Hình ảnh đồ ăn, thức uống") })
            ImagePickerField(
                imageState = ImageState(imageUri = unsavedImageUri),
                onImageClick = { imagePickerLauncher.launch("image/*") },
                onDeleteImage = { viewModel.setUnsavedImage(null) },
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
            )
            DividerWithSubhead(subhead = { Text("Thông tin đồ ăn, thức uống") })
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.setFoodName(it) },
                singleLine = true,
                label = { Text("Tên") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { viewModel.setFoodDescription(it) },
                supportingText = { Text("Không bắt buộc") },
                singleLine = true,
                label = { Text("Mô tả") },
                modifier = Modifier.fillMaxWidth()
            )
            CategoryAndVariationField(
                startIcon = Icons.Default.Sell,
                endIcon = Icons.Default.ChevronRight,
                value = basePrice,
                title = "Giá",
                placeholder = "Đặt",
                modifier = Modifier.padding(top = 8.dp),
                onClick = { showBottomSheet = true }
            )

            DividerWithSubhead(
                subhead = { Text("Danh mục và phân loại") },
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            CategoryAndVariationField(
                startIcon = Icons.Default.Fastfood,
                endIcon = Icons.Default.ChevronRight,
                value = currentCategory,
                title = "Danh mục",
                placeholder = "Trà sữa",
                onClick = { onNavigateToFoodCategory() }
            )
            Spacer(modifier = Modifier.height(2.dp))
            CategoryAndVariationField(
                title = "Phân loại",
                value = unsavedVariations.joinToString(", ") { it.name },
                startIcon = Icons.Default.Category,
                endIcon = Icons.Default.ChevronRight,
                placeholder = "Topping, size",
                onClick = { onNavigateToVariation() }
            )
        }
    }
}