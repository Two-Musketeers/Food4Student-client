package com.ilikeincest.food4student.screen.auth.select_role

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.component.LoadingDialog
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview

@Composable
fun SelectRoleUserScreen(
    onSuccessSignUp: () -> Unit,
    vm: SelectRoleUserViewModel = hiltViewModel()
) {
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
    Surface { Column(
        verticalArrangement = Arrangement.spacedBy(42.dp, Alignment.CenterVertically),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
    ) {
        Text("Suýt thì quên...", style = typography.displayMedium)
        var phone by rememberSaveable { mutableStateOf("") }
        TextField(
            value = phone, onValueChange = { phone = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                vm.registerUser(phone, onSuccessSignUp)
            },
            contentPadding = PaddingValues(28.dp, 12.dp, 18.dp, 12.dp),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Let's eat!", style = typography.titleMedium)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
        }
    } }
}

@Preview
@Composable
private fun Prev() { ScreenPreview {
    SelectRoleUserScreen({})
} }