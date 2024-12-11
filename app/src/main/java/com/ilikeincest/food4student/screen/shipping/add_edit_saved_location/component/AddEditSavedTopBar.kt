package com.ilikeincest.food4student.screen.shipping.add_edit_saved_location.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilikeincest.food4student.component.preview_helper.ComponentPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSavedTopBar(
    isEdit: Boolean,
    onNavigateUp: () -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    val title =
        if (isEdit) "Sửa địa chỉ"
        else "Thêm địa chỉ"
    TopAppBar(
        title = { Text(title) },
        navigationIcon = { IconButton(onClick = onNavigateUp) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Trở về")
        } },
        actions = {
            if (isEdit) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Outlined.Delete, "Xóa địa chỉ")
                }
            }
            FilledTonalButton(
                contentPadding = PaddingValues(start = 16.dp, top = 10.dp, end = 24.dp, bottom = 10.dp),
                onClick = onSave
            ) {
                Icon(Icons.Default.Check, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Lưu")
            }
            Spacer(Modifier.width(12.dp))
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Edit() { ComponentPreview {
    AddEditSavedTopBar(true, {}, {}, {}, TopAppBarDefaults.pinnedScrollBehavior())
} }

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Add() { ComponentPreview {
    AddEditSavedTopBar(false, {}, {}, {}, TopAppBarDefaults.pinnedScrollBehavior())
} }