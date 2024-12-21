package com.ilikeincest.food4student.screen.food_item.add_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.screen.food_item.ConfirmDeleteDialog
import com.ilikeincest.food4student.screen.food_item.add_category.component.CategoryDialog
import com.ilikeincest.food4student.screen.food_item.add_category.component.CategoryItem
import com.ilikeincest.food4student.screen.restaurant.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    onNavigateUp: () -> Unit,
    viewModel: RestaurantViewModel
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val categories = viewModel.categories.collectAsState().value
    val selectedFoodCategory by viewModel.selectedFoodCategory.collectAsState()

    var showCategoryDialog by remember { mutableStateOf(false) }
    var isEditingCategory by remember { mutableStateOf(false) }
    var currentCategoryIndex by remember { mutableIntStateOf(-1) }
    var categoryName by remember { mutableStateOf("") }
    var currentCategoryId by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.selectedFoodItem) {
        if (viewModel.selectedFoodItem.value != null) {
            viewModel.addFoodCategorySelectionMode(true)
        } else {
            viewModel.addFoodCategorySelectionMode(false)
            viewModel.selectFoodCategory(null)
        }
    }

    // Dialog for adding/editing a category
    if (showCategoryDialog) {
        CategoryDialog(
            isEditingCategory = isEditingCategory,
            categoryName = categoryName,
            onCategoryNameChange = { categoryName = it },
            onSaveCategory = {
                if (isEditingCategory && currentCategoryId != null) {
                    viewModel.updateCategory(currentCategoryId!!, categoryName)
                } else {
                    viewModel.addCategory(categoryName)
                }
                categoryName = ""
                isEditingCategory = false
                currentCategoryId = null
                showCategoryDialog = false
            },
            onDismissRequest = {
                categoryName = ""
                isEditingCategory = false
                currentCategoryId = null
                showCategoryDialog = false
            }
        )
    }

    // Confirmation dialog for deleting a category
    if (showDeleteConfirmation && currentCategoryIndex >= 0) {
        ConfirmDeleteDialog(
            title = "Xóa danh mục đồ ăn",
            text = "Bạn có chắc chắn muốn xóa danh mục đồ ăn này? Danh mục này và tất cả các món ăn bên trong sẽ bị xóa.",
            onConfirm = {
                currentCategoryId?.let { viewModel.removeCategory(it) }
                showDeleteConfirmation = false
                currentCategoryIndex = -1
                currentCategoryId = null
            },
            onDismiss = {
                showDeleteConfirmation = false
                currentCategoryIndex = -1
                currentCategoryId = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm danh mục đồ ăn") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Trở về")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.imePadding()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // List of categories
            categories.forEachIndexed { index, category ->
                CategoryItem(
                    category = category,
                    onEditCategory = {
                        isEditingCategory = true
                        currentCategoryIndex = index
                        currentCategoryId = category.id
                        categoryName = category.name
                        showCategoryDialog = true
                    },
                    onDeleteCategory = {
                        currentCategoryIndex = index
                        currentCategoryId = category.id
                        showDeleteConfirmation = true
                    },
                    onClick = {
                        viewModel.selectFoodCategory(category)
                        onNavigateUp()
                    },
                    isSelected = category.id == selectedFoodCategory?.id
                )
            }
            // "Thêm danh mục đồ ăn" button
            ElevatedCard(
                onClick = {
                    isEditingCategory = false
                    categoryName = ""
                    showCategoryDialog = true
                },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Thêm danh mục đồ ăn")
                    }
                }
            }
        }
    }
}

