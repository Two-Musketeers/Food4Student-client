package com.ilikeincest.food4student.util.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Use this to wrap your source screen. The one that uses the result.
 */
@Composable
inline fun <reified T : @Serializable Any> NavigateWithResult(
    currentBackStackEntry: NavBackStackEntry,
    content: @Composable (result: T?) -> Unit
) {
    val serializedData by currentBackStackEntry.savedStateHandle
        .getStateFlow<String?>("result", null)
        .collectAsState()
    val result = remember(serializedData) {
        if (serializedData != null)
            Json.decodeFromString<T>(serializedData!!)
        else null
    }
    content(result)
    LaunchedEffect(serializedData) {
        if (serializedData != null)
            currentBackStackEntry.savedStateHandle["result"] = null
    }
}

/**
 * Use this to pop back from the screen making the result.
 */
inline fun <reified T : @Serializable Any>
NavController.popBackWithResult(result: T) {
    this.previousBackStackEntry
        ?.savedStateHandle?.set("result", Json.encodeToString(result))
    this.popBackStack()
}