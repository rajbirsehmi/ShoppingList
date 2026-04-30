package com.creative.shoppinglist.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.creative.shoppinglist.ui.components.TestTags

class EmptyScreenRobot(private val composeTestRule: ComposeTestRule): BaseRobot(composeTestRule) {

    fun assertEmptyScreenMainMessageIsDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.EMPTY_SCREEN_MAIN_MESSAGE).assertIsDisplayed()
    }

    fun assertEmptyScreenSubMessageIsDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.EMPTY_SCREEN_SUB_MESSAGE).assertIsDisplayed()
    }

    fun assertEmptyScreenIconIsDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.EMPTY_SCREEN_ICON).assertIsDisplayed()
    }

    fun assertEmptyScreenAddButtonIsDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.EMPTY_SCREEN_ADD_BUTTON).assertIsDisplayed()
    }

    fun assertFabIsNotDisplayedWhenItemListIsEmpty() {
        composeTestRule.onNodeWithTag(TestTags.FAB_ADD_ITEM).assertDoesNotExist()
    }

}

fun emptyScreenRobot(
    composeTestRule: ComposeTestRule,
    func: EmptyScreenRobot.() -> Unit
) = EmptyScreenRobot(composeTestRule).apply { func() }
