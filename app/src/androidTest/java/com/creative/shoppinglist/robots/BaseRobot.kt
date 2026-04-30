package com.creative.shoppinglist.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

open class BaseRobot(private val composeTestRule: ComposeTestRule) {

    fun clickOn(tag: String) {
        composeTestRule.onNodeWithTag(tag).performClick()
    }

    fun waitForIdle() {
        composeTestRule.waitForIdle()
    }
}