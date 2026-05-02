plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.creative.shoppinglist.ui.tests"
    compileSdk = 36

    targetProjectPath = ":app"

    defaultConfig {
        minSdk = 33
        targetSdk = 35
        testInstrumentationRunner = "com.creative.shoppinglist.core.testing.HiltTestRunner"
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        managedDevices {
            localDevices {
                create("device1") {
                    device = "Pixel 2"
                    apiLevel = 33
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }
}

dependencies {
    implementation(project(":app"))
    implementation(project(":core-testing"))
    lintChecks(project(":lint"))
    
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.testing)
    ksp(libs.hilt.android.compiler)
    
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.test.junit4)
    
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.rules)
    
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    implementation(libs.kotlinx.serialization.json)
}
