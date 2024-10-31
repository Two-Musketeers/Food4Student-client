package com.ilikeincest.food4student.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

/**
 * A pre-defined normal text field that auto fill max width
 *
 * @param label Label of field
 * @param value Actual value inside field
 * @param onValueChange Callback invoked on password change
 * @param keyboardType Type of keyboard to show
 * @param imeAction Specifies which button to use for keyboard's enter key
 * @param keyboardAction KeyboardAction invoked when user press enter key
 */
@Composable
fun NormalField(
    label: String, value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    keyboardAction: KeyboardActions = KeyboardActions.Default,
) {
    TextField(
        value = value, onValueChange = onValueChange,
        singleLine = true,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardAction,
        modifier = Modifier.fillMaxWidth()
    )
}