package com.ilikeincest.food4student.screen.auth.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ilikeincest.food4student.component.PasswordField
import com.ilikeincest.food4student.screen.auth.component.AuthenticationButton
import com.ilikeincest.food4student.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavHostController,
    navigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val email by signUpViewModel.email.collectAsState()
    val password by signUpViewModel.password.collectAsState()
    val confirmPassword by signUpViewModel.confirmPassword.collectAsState()

    val emailError by signUpViewModel.emailError.collectAsState()
    val passwordError by signUpViewModel.passwordError.collectAsState()
    val confirmPasswordError by signUpViewModel.confirmPasswordError.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(42.dp, Alignment.CenterVertically),
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp, 10.dp)
    ) {
        Text(
            text = "Đăng ký",
            style = typography.displayMedium
        )

        // fields
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TextField(
                value = email, onValueChange = { signUpViewModel.updateEmail(it) },
                singleLine = true,
                label = { Text("email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = emailError != "",
                supportingText = if (emailError != "") {{ Text(emailError) }} else null,
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                "mật khẩu", password, { signUpViewModel.updatePassword(it) },
                imeAction = ImeAction.Next,
                isError = passwordError != "",
                errorMessage = passwordError,
            )
            PasswordField(
                "nhập lại mật khẩu", confirmPassword,
                { signUpViewModel.updateConfirmPassword(it) },
                isError = confirmPasswordError != "",
                errorMessage = confirmPasswordError
            )
        }

        // buttons
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { signUpViewModel.onSignUpClick(navController) }, modifier = Modifier.fillMaxWidth()) {
                Text("Let's eat!")
            }
            AuthenticationButton(
                buttonText = "Đăng nhập với Google",
                onGetCredentialResponse = { credential ->
                    signUpViewModel.onGoogleSignIn(navController, credential)
                }
            )
            OutlinedButton(onClick = { navigateToSignIn() }, modifier = Modifier.fillMaxWidth()) {
                Text("Đã có tài khoản?")
            }
        }
    }
}

//@PreviewLightDark
//@Composable
//private fun SignUpPrev() {lmao
//    ScreenPreview { SignUpScreen() }
//}