plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.creative.shoppinglist.core.testing"
    compileSdk = 36

    defaultConfig {
        minSdk = 33
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.androidx.compose.material3)
    
    // Core testing libraries needed by robots
    api(libs.androidx.compose.ui.test.junit4)
    api(libs.junit)

    api(libs.slf4j.api)
    implementation(libs.logback.android)

    // Hilt Testing
    implementation(libs.hilt.android.testing)
    implementation(libs.androidx.test.runner)
}
