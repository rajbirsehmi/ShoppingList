package com.creative.shoppinglist.core.testing.robots

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule

open class BaseRobot(protected val composeTestRule: ComposeTestRule) {

    fun clickOn(tag: String) {
        waitUntilVisible(tag)
        composeTestRule.onNodeWithTag(tag).performClick()
    }

    fun clickOnWithText(text: String) {
        waitUntilTextVisible(text)
        composeTestRule.onNodeWithText(text).performClick()
    }

    fun enterText(tag: String, text: String) {
        waitUntilVisible(tag)
        composeTestRule.onNodeWithTag(tag).performTextReplacement(text)
    }

    fun assertDisplayed(tag: String) {
        waitUntilVisible(tag)
        composeTestRule.onNodeWithTag(tag).assertIsDisplayed()
    }

    fun assertTextDisplayed(text: String) {
        waitUntilTextVisible(text)
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    fun assertDoesNotExist(tag: String) {
        waitUntilNotVisible(tag)
        composeTestRule.onNodeWithTag(tag).assertDoesNotExist()
    }

    fun performSwipeUp(tag: String) {
        waitUntilVisible(tag)
        composeTestRule.onNodeWithTag(tag).performTouchInput { swipeUp() }
    }

    fun performSwipeDown(tag: String) {
        waitUntilVisible(tag)
        composeTestRule.onNodeWithTag(tag).performTouchInput { swipeDown() }
    }

    fun waitForIdle() {
        composeTestRule.waitForIdle()
    }

    fun waitUntilVisible(tag: String, timeoutMillis: Long = 5000) {
        composeTestRule.waitUntil(timeoutMillis) {
            composeTestRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
        }
    }

    fun waitUntilNotVisible(tag: String, timeoutMillis: Long = 5000) {
        composeTestRule.waitUntil(timeoutMillis) {
            composeTestRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isEmpty()
        }
    }

    fun waitUntilTextVisible(text: String, timeoutMillis: Long = 5000) {
        composeTestRule.waitUntil(timeoutMillis) {
            composeTestRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
