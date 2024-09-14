package com.ilikeincest.food4student.screens.splash

import com.ilikeincest.food4student.model.service.AccountService
import com.ilikeincest.food4student.screens.ScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountService: AccountService
) : ScreenViewModel() {

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        if (accountService.hasUser()) openAndPopUp("AppScreen", "SplashScreen")
        else openAndPopUp("SignInScreen", "SplashScreen")
    }

}