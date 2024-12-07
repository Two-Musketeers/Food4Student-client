import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    // DI things
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    // login things
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
}

android {
    namespace = "com.ilikeincest.food4student"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ilikeincest.food4student"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(project.rootProject.file("secret.properties").inputStream())
        val apiKey = properties.getProperty("HERE_API_KEY")
        val apiSecretKey = properties.getProperty("HERE_API_SECRET_KEY")
        val backendUrl = properties.getProperty("BACKEND_URL")

        // Define the API key as a build config field
        buildConfigField("String", "HERE_API_KEY", "\"$apiKey\"")
        buildConfigField("String", "HERE_API_SECRET_KEY", "\"$apiSecretKey\"")
        buildConfigField("String", "BACKEND_URL", "\"$backendUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // clock things
    implementation(libs.human.readable)
    implementation(libs.kotlinx.datetime)

    // permission things
    implementation(libs.accompanist.permissions)

    // navigation things
    implementation(libs.androidx.navigation.compose)

    //Here library aar thing
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    //Take in current geo location
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // api things
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // google sign in
    implementation(libs.googleid)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)

    // DI - hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Firebase Auth and fcm
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)

    // view model
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // icons things
    implementation(libs.androidx.material.icons.extended)

    // online image things
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.test)
    implementation(platform(libs.coil.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}