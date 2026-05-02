package com.creative.shoppinglist.core.testing.robots

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.creative.shoppinglist.core.testing.TestTags
import com.creative.shoppinglist.core.testing.engine.RobustTestEngine

class ShoppingRobot(
    composeTestRule: ComposeTestRule,
    engine: RobustTestEngine = RobustTestEngine(composeTestRule)
) : BaseRobot(composeTestRule, engine) {

    fun openAddItemBottomSheet() {
        logger.info("[ENGINE] Opening Add Item bottom sheet")
        // Check if FAB is present with a short timeout to handle animations
        if (engine.isDisplayed(hasTestTag(TestTags.FAB_ADD_ITEM), timeout = 500)) {
            clickOn(TestTags.FAB_ADD_ITEM)
        } else {
            // Fallback to empty screen add button
            clickOn(TestTags.EMPTY_SCREEN_ADD_BUTTON)
        }
    }

    fun enterItemName(name: String) {
        logger.info("[ENGINE] Entering item name: {}", name)
        enterText(TestTags.ITEM_NAME_INPUT, name)
    }

    fun toggleImportant(isImportant: Boolean) {
        logger.info("[ENGINE] Setting importance to: {}", isImportant)
        engine.waitUntilVisible(hasTestTag(TestTags.ITEM_IMPORTANT_SWITCH), "Important Switch")
        val node = composeTestRule.onNodeWithTag(TestTags.ITEM_IMPORTANT_SWITCH)
        val isChecked = node.fetchSemanticsNode().config.getOrElse(SemanticsProperties.ToggleableState) { ToggleableState.Off } == ToggleableState.On
        if (isChecked != isImportant) {
            node.performClick()
        }
    }


    fun saveItem() {
        logger.info("[ENGINE] Saving item")
        clickOn(TestTags.SAVE_ITEM_BUTTON)
        waitUntilNotVisible(TestTags.BOTTOM_SHEET_ADD_ITEM)
    }

    fun assertItemDisplayed(name: String) {
        logger.info("[ENGINE] Asserting item '{}' is displayed", name)
        val regularTag = TestTags.SHOPPING_ITEM_CARD_REGULAR + name
        val importantTag = TestTags.SHOPPING_ITEM_CARD_IMPORTANT + name

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag(regularTag).fetchSemanticsNodes().isNotEmpty() ||
                    composeTestRule.onAllNodesWithTag(importantTag).fetchSemanticsNodes().isNotEmpty()
        }

        if (composeTestRule.onAllNodesWithTag(regularTag).fetchSemanticsNodes().isNotEmpty()) {
            assertDisplayed(regularTag)
        } else {
            assertDisplayed(importantTag)
        }
    }

    fun toggleItemChecked(name: String) {
        logger.info("[ENGINE] Toggling checkbox for item: {}", name)
        val checkboxTag = TestTags.SHOPPING_ITEM_CHECKBOX + name
        clickOn(checkboxTag)
    }

    fun deleteItemBySwipe(name: String) {
        logger.info("[ENGINE] Deleting item '{}' by swipe", name)
        val regularTag = TestTags.SHOPPING_ITEM_CARD_REGULAR + name
        val importantTag = TestTags.SHOPPING_ITEM_CARD_IMPORTANT + name

        val node = if (composeTestRule.onAllNodesWithTag(regularTag).fetchSemanticsNodes().isNotEmpty()) {
            composeTestRule.onNodeWithTag(regularTag)
        } else {
            composeTestRule.onNodeWithTag(importantTag)
        }

        node.performTouchInput {
            swipeLeft()
        }
        waitForIdle()
    }

    fun assertItemDoesNotExist(name: String) {
        logger.info("[ENGINE] Asserting item '{}' does not exist", name)
        assertDoesNotExist(TestTags.SHOPPING_ITEM_CARD_REGULAR + name)
        assertDoesNotExist(TestTags.SHOPPING_ITEM_CARD_IMPORTANT + name)
    }
}

fun shoppingRobot(composeTestRule: ComposeTestRule, func: ShoppingRobot.() -> Unit) =
    ShoppingRobot(composeTestRule).apply { func() }
