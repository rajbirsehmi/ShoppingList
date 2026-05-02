package com.creative.shoppinglist.core.testing.robots

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.creative.shoppinglist.core.testing.engine.RobustTestEngine
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BaseRobot(
    protected val composeTestRule: ComposeTestRule,
    protected val engine: RobustTestEngine = RobustTestEngine(composeTestRule)
) {
    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun clickOn(tag: String) {
        logger.info("[ENGINE] Clicking on element with tag: {}", tag)
        engine.click(hasTestTag(tag), tag)
    }

    fun clickOnWithText(text: String) {
        logger.info("[ENGINE] Clicking on element with text: {}", text)
        engine.click(hasText(text), text)
    }

    fun enterText(tag: String, text: String) {
        logger.info("[ENGINE] Entering text '{}' into element with tag: {}", text, tag)
        engine.type(hasTestTag(tag), text, tag)
    }

    fun assertDisplayed(tag: String) {
        logger.info("[ENGINE] Asserting element with tag is displayed: {}", tag)
        engine.assertVisible(hasTestTag(tag), tag)
    }

    fun assertTextDisplayed(text: String) {
        logger.info("[ENGINE] Asserting text is displayed: {}", text)
        engine.assertVisible(hasText(text), text)
    }

    fun assertDoesNotExist(tag: String) {
        logger.info("[ENGINE] Asserting element with tag does not exist: {}", tag)
        engine.assertDoesNotExist(hasTestTag(tag), tag)
    }

    fun performSwipeUp(tag: String) {
        logger.info("[ENGINE] Performing swipe up on element with tag: {}", tag)
        engine.assertVisible(hasTestTag(tag), tag)
        composeTestRule.onNodeWithTag(tag).performTouchInput { swipeUp() }
    }

    fun performSwipeDown(tag: String) {
        logger.info("[ENGINE] Performing swipe down on element with tag: {}", tag)
        engine.assertVisible(hasTestTag(tag), tag)
        composeTestRule.onNodeWithTag(tag).performTouchInput { swipeDown() }
    }

    fun waitForIdle() {
        logger.info("[ENGINE] Waiting for Compose idle")
        composeTestRule.waitForIdle()
    }

    fun waitUntilVisible(tag: String, timeoutMillis: Long = 5000) {
        logger.info("[ENGINE] Waiting until element with tag is visible: {}", tag)
        engine.assertVisible(hasTestTag(tag), tag)
    }

    fun waitUntilNotVisible(tag: String, timeoutMillis: Long = 5000) {
        logger.info("[ENGINE] Waiting until element with tag is not visible: {}", tag)
        engine.assertDoesNotExist(hasTestTag(tag), tag)
    }

    fun waitUntilTextVisible(text: String, timeoutMillis: Long = 5000) {
        logger.info("[ENGINE] Waiting until text is visible: {}", text)
        engine.assertVisible(hasText(text), text)
    }
}

