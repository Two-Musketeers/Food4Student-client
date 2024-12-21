package com.ilikeincest.food4student.screen.main_page

import androidx.lifecycle.ViewModel
import com.ilikeincest.food4student.model.Account
import com.ilikeincest.food4student.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {
    private val _profile = MutableStateFlow(Account())
    val profile = _profile.asStateFlow()

    fun refreshUserProfile() {
        _profile.value = accountService.getUserProfile()
    }
}