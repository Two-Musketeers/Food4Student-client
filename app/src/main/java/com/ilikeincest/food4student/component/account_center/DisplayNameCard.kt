package com.ilikeincest.food4student.component.account_center

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ilikeincest.food4student.R

@Composable
fun DisplayNameCard(displayName: String, onUpdateDisplayNameClick: (String) -> Unit) {
    var showDisplayNameDialog by remember { mutableStateOf(false) }
    var newDisplayName by remember { mutableStateOf(displayName) }

    val cardTitle = displayName.ifBlank { stringResource(R.string.profile_name) }

    AccountCenterCard(cardTitle, Icons.Filled.Edit, Modifier.card()) {
        newDisplayName =  displayName
        showDisplayNameDialog = true
    }

    if (showDisplayNameDialog) {
        AlertDialog(
            title = { Text(stringResource(R.string.profile_name)) },
            text = {
                Column {
                    TextField(
                        value = newDisplayName,
                        onValueChange = { newDisplayName = it }
                    )
                }
            },
            dismissButton = {
                Button(onClick = { showDisplayNameDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                Button(onClick = {
                    onUpdateDisplayNameClick(newDisplayName)
                    showDisplayNameDialog = false
                }) {
                    Text(text = stringResource(R.string.update))
                }
            },
            onDismissRequest = { showDisplayNameDialog = false }
        )
    }
}
