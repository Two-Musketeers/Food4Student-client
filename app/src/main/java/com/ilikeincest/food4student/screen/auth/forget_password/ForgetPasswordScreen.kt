package com.ilikeincest.food4student.screen.auth.forget_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.component.LoadingDialog

@Composable
fun ForgetPasswordScreen(
    onNavigateUp: () -> Unit,
    vm: ForgetPasswordViewModel = hiltViewModel()
) {
    val showLoading by vm.isLoading
    val isSuccess by vm.isSuccess
    var message by vm.dialogMessage

    LoadingDialog(
        label = "Bạn chờ tí nha...",
        isVisible = showLoading
    )
    val title = if (isSuccess) "Thành công!" else "Lỗi!"
    val onDismiss = {
        if (isSuccess) onNavigateUp()
        else message = ""
    }
    val icon = if (isSuccess) Icons.Default.CheckCircle
        else Icons.Default.Error
    if (message.isNotBlank()) {
        AlertDialog(
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            },
            onDismissRequest = onDismiss,
            icon = { Icon(icon, null) },
        )
    }

    Surface { Column(
        verticalArrangement = Arrangement.spacedBy(42.dp, Alignment.CenterVertically),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
    ) {
        Text("Còn cứu được...", style = typography.displayMedium)
        var email by rememberSaveable { mutableStateOf("") }
        TextField(
            value = email, onValueChange = { email = it },
            label = { Text("email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
            ),
            keyboardActions = KeyboardActions {
                vm.forgetPassword(email)
            },
            modifier = Modifier.fillMaxWidth()
        )
        val enableButton by remember { derivedStateOf { email.isNotBlank() } }
        Button(
            onClick = {
                vm.forgetPassword(email)
            },
            enabled = enableButton,
            contentPadding = PaddingValues(28.dp, 12.dp, 18.dp, 12.dp),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Let's eat!", style = typography.titleMedium)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
        }
    } }
}