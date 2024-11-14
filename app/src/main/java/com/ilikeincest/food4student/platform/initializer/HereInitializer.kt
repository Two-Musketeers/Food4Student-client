package com.ilikeincest.food4student.platform.initializer

import android.content.Context
import com.here.sdk.core.engine.SDKNativeEngine
import com.here.sdk.core.engine.SDKOptions
import com.here.sdk.core.errors.InstantiationErrorException
import com.ilikeincest.food4student.BuildConfig

fun initializeHERESDK(context: Context) {
    val accessKeyID = BuildConfig.HERE_API_KEY
    val accessKeySecret = BuildConfig.HERE_API_SECRET_KEY
    val options = SDKOptions(accessKeyID, accessKeySecret)
    try {
        SDKNativeEngine.makeSharedInstance(context, options)
    } catch (e: InstantiationErrorException) {
        throw RuntimeException("Initialization of HERE SDK failed: " + e.error.name)
    }
}

fun disposeHERESDK() {
    SDKNativeEngine.getSharedInstance()?.dispose()
}