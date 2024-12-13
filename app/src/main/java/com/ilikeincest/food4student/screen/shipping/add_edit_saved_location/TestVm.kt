package com.ilikeincest.food4student.screen.shipping.add_edit_saved_location

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ilikeincest.food4student.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// test vm to show you how to get shit from nav args inside vm
@HiltViewModel
// take in a SavedStateHandle
class TestVm @Inject constructor(private val handle: SavedStateHandle) : ViewModel() {
    fun test() {
        // get shit with handle["put arg name here"]
        // keep in mind you need to specify the type yourself.
        // yes it will be nullable glhf
        Log.d("Test", handle["id"] ?: "")
        // do not hard code, use the route props' name instead, in case we change it
        Log.d("Test", AppRoutes.EditSavedLocation::id.name) // returns "id"
        // basically it looks like this, if its int for example
        val a: Int? = handle[AppRoutes.EditSavedLocation::id.name]
    }
}