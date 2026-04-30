package com.creative.shoppinglist.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.creative.shoppinglist.ui.components.TestTags

class HomeScreenRobot(private val composeTestRule: ComposeTestRule) {

    fun assertHomeScreenTitleIsDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.MAIN_SCREEN_TOP_APP_BAR).assertIsDisplayed()
    }

    fun assertBottonNavItemsAreDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.BOTTOM_NAV_ITEM + "Regular").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.BOTTOM_NAV_ITEM + "Important").assertIsDisplayed()
    }

    fun assertFabIsDisplayed() {
        if (composeTestRule.onNodeWithTag(TestTags.EMPTY_SCREEN_ADD_BUTTON).isNotDisplayed())
            composeTestRule.onNodeWithTag(TestTags.FAB_ADD_ITEM).assertIsDisplayed()
    }
}

fun homeScreenRobot(
    composeTestRule: ComposeTestRule,
    func: HomeScreenRobot.() -> Unit
) = HomeScreenRobot(composeTestRule).apply { func() }