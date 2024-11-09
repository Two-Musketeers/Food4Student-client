package com.ilikeincest.food4student.screen.auth.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ilikeincest.food4student.R
import com.ilikeincest.food4student.component.NormalField
import com.ilikeincest.food4student.component.PasswordField
import com.ilikeincest.food4student.component.preview_helper.ScreenPreview
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
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(42.dp, Alignment.CenterVertically),
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp, 10.dp)
    ) {
        // TODO: add error messages
        Text(
            text = "Đăng ký",
            style = typography.displayMedium
        )

        // fields
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            NormalField("email", email, { signUpViewModel.updateEmail(it) }, KeyboardType.Email)
            PasswordField("mật khẩu", password, { signUpViewModel.updatePassword(it) })
            PasswordField("nhập lại mật khẩu", confirmPassword, { signUpViewModel.updateConfirmPassword(it) })
        }

        // buttons
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { signUpViewModel.onSignUpClick(navController, context) }, modifier = Modifier.fillMaxWidth()) {
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