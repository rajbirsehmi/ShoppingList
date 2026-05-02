package com.creative.shoppinglist.core.testing.engine

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule

/**
 * A scalable, stateless engine for Android UI Automation.
 */
class RobustTestEngine(
    private val composeRule: ComposeTestRule,
    private val config: EngineConfig = EngineConfig()
) {
    private val TAG = "RobustEngine"

    data class EngineConfig(
        val defaultTimeout: Long = 5000L,
        val interactionDelay: Long = 0L,
        val screenshotOnFailure: Boolean = true
    )

    fun click(matcher: SemanticsMatcher, label: String) {
        log("Clicking: $label")
        executeWithRetry(label) {
            waitUntilVisible(matcher, label)
            composeRule.onNode(matcher).performClick()
        }
    }

    fun isDisplayed(matcher: SemanticsMatcher, timeout: Long = 0L): Boolean {
        return try {
            if (timeout > 0) {
                composeRule.waitUntil(timeout) {
                    composeRule.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
                }
            }
            composeRule.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
        } catch (e: Throwable) {
            false
        }
    }

    fun waitUntilVisible(matcher: SemanticsMatcher, label: String, timeout: Long = config.defaultTimeout) {
        try {
            composeRule.waitUntil(timeout) {
                composeRule.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
            }
        } catch (e: Throwable) {
            log("TIMEOUT: $label not visible after $timeout ms")
            throw e
        }
    }

    fun type(matcher: SemanticsMatcher, text: String, label: String) {
        log("Typing '$text' into: $label")
        executeWithRetry(label) {
            val node = composeRule.onNode(matcher)
            node.performTextClearance()
            node.performTextInput(text)
            node.assertTextContains(text)
        }
    }

    fun scrollTo(matcher: SemanticsMatcher, label: String) {
        log("Scrolling to: $label")
        executeWithRetry(label) {
            composeRule.onNode(matcher).performScrollTo()
        }
    }

    fun assertVisible(matcher: SemanticsMatcher, label: String) {
        log("Asserting visibility: $label")
        try {
            waitUntilVisible(matcher, label)
            composeRule.onNode(matcher).assertIsDisplayed()
        } catch (e: Throwable) {
            handleFailure(label, e)
        }
    }

    fun assertDoesNotExist(matcher: SemanticsMatcher, label: String) {
        log("Asserting does not exist: $label")
        try {
            waitUntilDoesNotExist(matcher, label)
            composeRule.onNode(matcher).assertDoesNotExist()
        } catch (e: Throwable) {
            handleFailure(label, e)
        }
    }

    fun waitUntilDoesNotExist(matcher: SemanticsMatcher, label: String, timeout: Long = config.defaultTimeout) {
        try {
            composeRule.waitUntil(timeout) {
                composeRule.onAllNodes(matcher).fetchSemanticsNodes().isEmpty()
            }
        } catch (e: Throwable) {
            log("TIMEOUT: $label still exists after $timeout ms")
            throw e
        }
    }

    fun waitForLoadingToFinish(loadingMatcher: SemanticsMatcher) {
        log("Waiting for loading state to finish...")
        composeRule.waitUntil(10000L) {
            composeRule.onAllNodes(loadingMatcher).fetchSemanticsNodes().isEmpty()
        }
    }

    private fun executeWithRetry(label: String, maxRetries: Int = 2, action: () -> Unit) {
        var lastError: Throwable? = null
        repeat(maxRetries) { attempt ->
            try {
                action()
                return
            } catch (e: Throwable) {
                lastError = e
                log("Attempt ${attempt + 1} failed for $label: ${e.localizedMessage}")
                composeRule.waitForIdle()
            }
        }
        handleFailure(label, lastError ?: RuntimeException("Unknown error"))
    }

    private fun handleFailure(label: String, error: Throwable) {
        log("ERROR on $label: ${error.localizedMessage}")
        // Future: Logic to trigger screenshot via composeRule
        throw error
    }

    private fun log(msg: String) = println("$TAG: $msg")
}
