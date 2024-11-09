package com.ilikeincest.food4student.component.account_center

import com.ilikeincest.food4student.R
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun RemoveAccountCard(navController: NavController, context: Context, onDeleteAccountClick: (NavController, Context) -> Unit) {
    var showRemoveAccDialog by remember { mutableStateOf(false) }

    AccountCenterCard(stringResource(R.string.delete_account), Icons.Filled.Delete, Modifier.card()) {
        showRemoveAccDialog = true
    }

    if (showRemoveAccDialog) {
        AlertDialog(
            title = { Text(stringResource(R.string.delete_account_title)) },
            text = { Text(stringResource(R.string.delete_account_description)) },
            dismissButton = {
                Button(onClick = { showRemoveAccDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                Button(onClick = {
                    onDeleteAccountClick(navController, context)
                    showRemoveAccDialog = false
                }) {
                    Text(text = stringResource(R.string.delete_account))
                }
            },
            onDismissRequest = { showRemoveAccDialog = false }
        )
    }
}