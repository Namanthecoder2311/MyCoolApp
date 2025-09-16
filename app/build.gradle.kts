plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Removed: alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.example.neokotlinuiconverted"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.neokotlinuiconverted"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true   // ✅ Enabled view binding
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3) // For Jetpack Compose
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.google.android.material:material:1.13.0") // Added for View system Material Components

    // Koin
    implementation("io.insert-koin:koin-android:3.5.6")             // Core Android, includes ViewModel support
    implementation("io.insert-koin:koin-androidx-navigation:3.5.6") // For Jetpack Navigation
    implementation("io.insert-koin:koin-androidx-compose:3.5.6")    // For Jetpack Compose

    // Unit test libs
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0") // Added
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1") // Updated
    testImplementation("app.cash.turbine:turbine:1.0.0") // Added
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0") // Updated

    // instrumentation test dependencies
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1") // optional
    androidTestImplementation("org.hamcrest:hamcrest:2.2")

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}