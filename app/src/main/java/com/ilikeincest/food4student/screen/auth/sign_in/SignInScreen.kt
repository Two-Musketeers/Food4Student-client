package com.ilikeincest.food4student.screen.auth.sign_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ilikeincest.food4student.component.NormalField
import com.ilikeincest.food4student.component.PasswordField
import com.ilikeincest.food4student.screen.auth.component.AuthenticationButton
import com.ilikeincest.food4student.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    navController: NavHostController,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    // big ass TODO: add event callbacks
    val email by signInViewModel.email.collectAsState()
    val password by signInViewModel.password.collectAsState()
    
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp, 10.dp)
    ) {
        Text(
            text = "Đăng nhập",
            style = typography.displayMedium,
            modifier = Modifier.padding(bottom = 42.dp)
        )

        // TODO: add email validation
        // TODO: add wrong creds warning
        NormalField(
            label = "email",
            value = email,
            onValueChange = { signInViewModel.updateEmail(it) },
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(24.dp))
        PasswordField("password", password, { signInViewModel.updatePassword(it) })
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(onClick = {}, modifier = Modifier.align(Alignment.End)) {
            Text("quên mật khẩu?")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { signInViewModel.onSignInClick(navController) },
                modifier = Modifier.fillMaxWidth()) {
                Text("Let's eat!")
            }
            AuthenticationButton(
                buttonText = "Đăng nhập với Google",
                onGetCredentialResponse = { credential ->
                    signInViewModel.onGoogleSignIn(navController, credential)
                }
            )
            OutlinedButton(onClick = { onNavigateToSignUp() }, modifier = Modifier.fillMaxWidth()) {
                Text("Đăng ký?")
            }
        }
    }
}

//@PreviewLightDark
//@Composable
//private fun SignInPrev() {
//    ScreenPreview {
//        SignInScreen({}, {})
//    }
//}