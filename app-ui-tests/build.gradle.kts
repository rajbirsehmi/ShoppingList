plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.creative.shoppinglist.ui.tests"
    compileSdk = 36

    targetProjectPath = ":app"

    defaultConfig {
        minSdk = 33
        targetSdk = 35
        testInstrumentationRunner = "com.creative.shoppinglist.HiltTestRunner"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":app"))
    implementation(project(":core-testing"))
    lintChecks(project(":lint"))
    
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.testing)
    ksp(libs.hilt.android.compiler)
    
    implementation(libs.androidx.compose.ui.test.junit4)
    
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.rules)
}
