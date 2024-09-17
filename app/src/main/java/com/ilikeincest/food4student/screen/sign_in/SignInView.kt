package com.ilikeincest.food4student.screen.sign_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showSystemUi = true)
@Composable
fun SignInScreen(modifier: Modifier = Modifier) {
    // big ass TODO: add event callbacks
    var isPasswordVisible by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    val showPasswordIcon =
        if (isPasswordVisible) Icons.Default.VisibilityOff
        else Icons.Default.Visibility
    val showPasswordContentDesc =
        if (isPasswordVisible) "Hide password"
        else "Show password"

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp, 10.dp)
    ) {
        Text(
            text = "đăng nhập",
            style = typography.displayMedium
        )
        Spacer(modifier = Modifier.height(42.dp))
        // TODO: add error messages
        TextField(
            value = username, onValueChange = { username = it },
            singleLine = true,
            label = { Text("email/số điện thoại") },
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            value = password, onValueChange = { password = it },
            singleLine = true,
            label = { Text("password") },
            trailingIcon = {
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {
                    Icon(
                        imageVector = showPasswordIcon,
                        contentDescription = showPasswordContentDesc
                    )
                }
            },
            visualTransformation =
                if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(onClick = {}, modifier = Modifier.align(Alignment.End)) {
            Text("quên mật khẩu?")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("let's eat!")
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("đăng ký?")
        }
    }
}