package com.creative.shoppinglist

import android.app.Application
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

    override fun onStart() {
        val uiAutomation = InstrumentationRegistry.getInstrumentation().uiAutomation
        
        // Disable animations synchronously to speed up tests and reduce flakiness
        uiAutomation.executeShellCommand("settings put global window_animation_scale 0")
        uiAutomation.executeShellCommand("settings put global transition_animation_scale 0")
        uiAutomation.executeShellCommand("settings put global animator_duration_scale 0")
        
        // Grant permissions once for all tests to avoid overhead of GrantPermissionRule in each test
        uiAutomation.executeShellCommand("pm grant com.creative.shoppinglist android.permission.POST_NOTIFICATIONS")

        super.onStart()
    }

    override fun onDestroy() {
        try {
            val uiAutomation = InstrumentationRegistry.getInstrumentation().uiAutomation
            // Restore animations
            uiAutomation.executeShellCommand("settings put global window_animation_scale 1")
            uiAutomation.executeShellCommand("settings put global transition_animation_scale 1")
            uiAutomation.executeShellCommand("settings put global animator_duration_scale 1")
        } catch (e: Exception) {
            // Best effort restoration
        }
        super.onDestroy()
    }
}
