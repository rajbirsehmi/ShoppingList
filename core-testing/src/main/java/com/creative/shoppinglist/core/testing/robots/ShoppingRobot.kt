package com.creative.shoppinglist.core.testing.robots

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.creative.shoppinglist.core.testing.TestTags

class ShoppingRobot(composeTestRule: ComposeTestRule) : BaseRobot(composeTestRule) {

    fun openAddItemBottomSheet() {
        // Can be opened via FAB or Empty Screen button
        try {
            clickOn(TestTags.FAB_ADD_ITEM)
        } catch (e: Exception) {
            clickOn(TestTags.EMPTY_SCREEN_ADD_BUTTON)
        }
    }

    fun enterItemName(name: String) {
        enterText(TestTags.ITEM_NAME_INPUT, name)
    }

    fun toggleImportant(isImportant: Boolean) {
        val node = composeTestRule.onNodeWithTag(TestTags.ITEM_IMPORTANT_SWITCH)
        val isChecked = node.fetchSemanticsNode().config.getOrElse(SemanticsProperties.ToggleableState) { ToggleableState.Off } == ToggleableState.On
        if (isChecked != isImportant) {
            node.performClick()
        }
    }


    fun saveItem() {
        clickOn(TestTags.SAVE_ITEM_BUTTON)
        waitUntilNotVisible(TestTags.BOTTOM_SHEET_ADD_ITEM)
    }

    fun assertItemDisplayed(name: String) {
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
        val checkboxTag = TestTags.SHOPPING_ITEM_CHECKBOX + name
        clickOn(checkboxTag)
    }

    fun deleteItemBySwipe(name: String) {
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
        assertDoesNotExist(TestTags.SHOPPING_ITEM_CARD_REGULAR + name)
        assertDoesNotExist(TestTags.SHOPPING_ITEM_CARD_IMPORTANT + name)
    }
}

fun shoppingRobot(composeTestRule: ComposeTestRule, func: ShoppingRobot.() -> Unit) =
    ShoppingRobot(composeTestRule).apply { func() }
