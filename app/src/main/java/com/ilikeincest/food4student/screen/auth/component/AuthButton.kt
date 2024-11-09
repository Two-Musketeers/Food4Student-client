package com.ilikeincest.food4student.screen.auth.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.ilikeincest.food4student.ACCOUNT_ERROR_TAG
import com.ilikeincest.food4student.R
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
private fun AuthButtonPreview() {
    AuthenticationButton(buttonText = "Login with Google bitch", {})
}

@Composable
fun AuthenticationButton(
    buttonText: String,
    onGetCredentialResponse: (Credential) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    val signInWithGoogleOption = remember {
        GetSignInWithGoogleOption
            .Builder(context.getString(R.string.google_console_client_id))
            .build()
    }
    val request = remember {
        GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .setPreferImmediatelyAvailableCredentials(false)
            .build()
    }

    OutlinedButton(
        onClick = {
            coroutineScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = context
                    )

                    onGetCredentialResponse(result.credential)
                } catch (e: GetCredentialException) {
                    Log.d(ACCOUNT_ERROR_TAG, e.message.orEmpty())
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(painterResource(R.drawable.google_g), null, tint = Color.Unspecified)
        Text(buttonText)
    }
}