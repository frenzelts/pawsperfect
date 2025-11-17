plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.frenzelts.pawsperfect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.frenzelts.pawsperfect"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    // ---- Compose ----
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.material3)
    implementation(libs.androidx.material)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.androidx.activity.compose)

    // ------ Navigation ------
    implementation(libs.androidx.navigation.compose)

    // ---- ViewModel + Compose ----
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // ---- Dependency Injection ----
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // ---- Local Storage ----
    implementation(libs.datastore.preferences)

    // ---- Networking ----
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson.converter)
    implementation(libs.gson)

    // ---- Images ----
    implementation(libs.coil.compose)

    // ---- Core ----
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // ---- Testing ----
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}