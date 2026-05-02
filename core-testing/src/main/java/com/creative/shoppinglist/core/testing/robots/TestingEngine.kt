package com.creative.shoppinglist.core.testing.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.creative.shoppinglist.core.testing.engine.RobustTestEngine

/**
 * Entry point for the UI Testing Robot DSL.
 * This allows for a more readable and scalable way to write UI tests.
 */
class TestingEngine(
    private val composeTestRule: ComposeTestRule,
    private val config: RobustTestEngine.EngineConfig = RobustTestEngine.EngineConfig()
) {

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
}

/**
 * Extension function to start the Testing Engine from a ComposeTestRule.
 */
fun ComposeTestRule.launchTestingEngine(
    config: RobustTestEngine.EngineConfig = RobustTestEngine.EngineConfig(),
    func: TestingEngine.() -> Unit
) {
    TestingEngine(this, config).apply(func)
}

