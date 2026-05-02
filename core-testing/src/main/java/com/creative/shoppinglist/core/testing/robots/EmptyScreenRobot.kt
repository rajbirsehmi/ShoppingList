package com.creative.shoppinglist.core.testing.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.creative.shoppinglist.core.testing.TestTags
import com.creative.shoppinglist.core.testing.engine.RobustTestEngine

class EmptyScreenRobot(
    composeTestRule: ComposeTestRule,
    engine: RobustTestEngine = RobustTestEngine(composeTestRule)
): BaseRobot(composeTestRule, engine) {

    fun assertEmptyScreenMainMessageIsDisplayed() {
        assertDisplayed(TestTags.EMPTY_SCREEN_MAIN_MESSAGE)
    }

    fun assertEmptyScreenSubMessageIsDisplayed() {
        assertDisplayed(TestTags.EMPTY_SCREEN_SUB_MESSAGE)
    }

    fun assertEmptyScreenIconIsDisplayed() {
        assertDisplayed(TestTags.EMPTY_SCREEN_ICON)
    }

    fun assertEmptyScreenAddButtonIsDisplayed() {
        assertDisplayed(TestTags.EMPTY_SCREEN_ADD_BUTTON)
    }

    fun assertFabIsNotDisplayedWhenItemListIsEmpty() {
        assertDoesNotExist(TestTags.FAB_ADD_ITEM)
    }

}

fun emptyScreenRobot(
    composeTestRule: ComposeTestRule,
    func: EmptyScreenRobot.() -> Unit
) = EmptyScreenRobot(composeTestRule).apply { func() }
