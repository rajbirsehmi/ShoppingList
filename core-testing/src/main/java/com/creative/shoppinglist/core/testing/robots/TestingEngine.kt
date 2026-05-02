package com.creative.shoppinglist.core.testing.robots

import androidx.compose.ui.test.junit4.ComposeTestRule

/**
 * Entry point for the UI Testing Robot DSL.
 * This allows for a more readable and scalable way to write UI tests.
 */
class TestingEngine(private val composeTestRule: ComposeTestRule) {

    fun onShoppingScreen(func: ShoppingRobot.() -> Unit): TestingEngine {
        ShoppingRobot(composeTestRule).apply(func)
        return this
    }

    fun onEmptyScreen(func: EmptyScreenRobot.() -> Unit): TestingEngine {
        EmptyScreenRobot(composeTestRule).apply(func)
        return this
    }

    fun onHomeScreen(func: HomeScreenRobot.() -> Unit): TestingEngine {
        HomeScreenRobot(composeTestRule).apply(func)
        return this
    }
    
    // Add more robots here as the app grows
}

/**
 * Extension function to start the Testing Engine from a ComposeTestRule.
 */
fun ComposeTestRule.launchTestingEngine(func: TestingEngine.() -> Unit) {
    TestingEngine(this).apply(func)
}
