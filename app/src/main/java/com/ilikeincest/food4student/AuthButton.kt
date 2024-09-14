package com.ilikeincest.food4student

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.ilikeincest.food4student.ui.theme.Purple40
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

    Button(
        onClick = {
            val googleIdOption = GetSignInWithGoogleOption
                .Builder(context.getString(R.string.google_console_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .setPreferImmediatelyAvailableCredentials(false)
                .build()

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
        Icon(
            // TODO: extract icon
            painter = painterResource(id = R.drawable.google_g),
            modifier = Modifier
                .padding(end = 16.dp)
                .height(18.dp),
            contentDescription = null
        )
        Text(
            text = buttonText,
            style = typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}