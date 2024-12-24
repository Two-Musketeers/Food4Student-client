package com.ilikeincest.food4student.screen.auth.sign_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilikeincest.food4student.component.ErrorDialog
import com.ilikeincest.food4student.component.NormalField
import com.ilikeincest.food4student.component.PasswordField
import com.ilikeincest.food4student.screen.auth.component.AuthenticationButton
import com.ilikeincest.food4student.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    onSetRootSplash: () -> Unit,
    onNavigateToForgetPassword: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel()
) {
    // big ass TODO: add event callbacks
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    var error by viewModel.error
    if (error != "") {
        ErrorDialog(error, { error = "" })
    }

    Surface { Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(32.dp, 10.dp)
    ) {
        Text(
            text = "Đăng nhập",
            style = typography.displayMedium,
            modifier = Modifier.padding(bottom = 42.dp)
        )

        NormalField(
            label = "email",
            value = email,
            onValueChange = { viewModel.setEmail(it) },
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(24.dp))
        PasswordField(
            label = "password",
            password = password,
            imeAction = ImeAction.Done,
            keyboardAction = KeyboardActions(onDone = {
                viewModel.onSignIn(onSuccess = onSetRootSplash)
            }),
            onPasswordChange = { viewModel.setPassword(it) }
        )
        Spacer(Modifier.height(4.dp))
        TextButton(
            onClick = onNavigateToForgetPassword,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("quên mật khẩu?")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { viewModel.onSignIn(onSuccess = onSetRootSplash) },
                modifier = Modifier.fillMaxWidth()) {
                Text("Let's eat!")
            }
            AuthenticationButton(
                buttonText = "Đăng nhập với Google",
                onGetCredentialResponse = { credential ->
                    viewModel.onGoogleSignIn(credential, onSuccess = onSetRootSplash)
                }
            )
            OutlinedButton(
                onClick = { onNavigateToSignUp() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Đăng ký?")
            }
        }
    } }
}