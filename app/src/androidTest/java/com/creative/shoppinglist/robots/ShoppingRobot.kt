package com.creative.shoppinglist.robots

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.creative.shoppinglist.ui.components.TestTags

class ShoppingRobot(private val composeTestRule: ComposeTestRule) : BaseRobot(composeTestRule) {

    fun openAddItemBottomSheet() {
        // Can be opened via FAB or Empty Screen button
        try {
            composeTestRule.onNodeWithTag(TestTags.FAB_ADD_ITEM).performClick()
        } catch (e: AssertionError) {
            composeTestRule.onNodeWithTag(TestTags.EMPTY_SCREEN_ADD_BUTTON).performClick()
        }
    }

    fun enterItemName(name: String) {
        composeTestRule.onNodeWithTag(TestTags.ITEM_NAME_INPUT).performTextInput(name)
    }

    fun toggleImportant(isImportant: Boolean) {
        val node = composeTestRule.onNodeWithTag(TestTags.ITEM_IMPORTANT_SWITCH)
        val isChecked = node.fetchSemanticsNode().config.getOrElse(SemanticsProperties.ToggleableState) { ToggleableState.Off } == ToggleableState.On
        if (isChecked != isImportant) {
            node.performClick()
        }
    }


    fun saveItem() {
        composeTestRule.onNodeWithTag(TestTags.SAVE_ITEM_BUTTON).performClick()
        composeTestRule.waitForIdle()
        // Wait for bottom sheet to be dismissed
        composeTestRule.onNodeWithTag(TestTags.BOTTOM_SHEET_ADD_ITEM).assertDoesNotExist()
    }

    fun assertItemDisplayed(name: String) {
        val regularTag = TestTags.SHOPPING_ITEM_CARD_REGULAR + name
        val importantTag = TestTags.SHOPPING_ITEM_CARD_IMPORTANT + name

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag(regularTag).fetchSemanticsNodes().isNotEmpty() ||
                    composeTestRule.onAllNodesWithTag(importantTag).fetchSemanticsNodes().isNotEmpty()
        }

        if (composeTestRule.onAllNodesWithTag(regularTag).fetchSemanticsNodes().isNotEmpty()) {
            composeTestRule.onNodeWithTag(regularTag).assertIsDisplayed()
        } else {
            composeTestRule.onNodeWithTag(importantTag).assertIsDisplayed()
        }
    }

    fun toggleItemChecked(name: String) {
        // Try to find either regular or important checkbox
        val regularTag = TestTags.SHOPPING_ITEM_CHECKBOX + name
        composeTestRule.onNodeWithTag(regularTag).performClick()
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
    }

    fun assertItemDoesNotExist(name: String) {
        composeTestRule.onNodeWithTag(TestTags.SHOPPING_ITEM_CARD_REGULAR + name).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.SHOPPING_ITEM_CARD_IMPORTANT + name).assertDoesNotExist()
    }
}

fun shoppingRobot(composeTestRule: ComposeTestRule, func: ShoppingRobot.() -> Unit) =
    ShoppingRobot(composeTestRule).apply { func() }

