package com.creative.shoppinglist.core.testing.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.creative.shoppinglist.core.testing.TestTags

class HomeScreenRobot(composeTestRule: ComposeTestRule) : BaseRobot(composeTestRule) {

    fun assertHomeScreenTitleIsDisplayed() {
        assertDisplayed(TestTags.MAIN_SCREEN_TOP_APP_BAR)
    }

    fun assertBottomNavItemsAreDisplayed() {
        assertDisplayed(TestTags.BOTTOM_NAV_ITEM + "Regular")
        assertDisplayed(TestTags.BOTTOM_NAV_ITEM + "Important")
    }

    fun assertFabIsDisplayed() {
        try {
            assertDisplayed(TestTags.FAB_ADD_ITEM)
        } catch (e: Exception) {
            assertDisplayed(TestTags.EMPTY_SCREEN_ADD_BUTTON)
        }
    }
}

fun homeScreenRobot(
    composeTestRule: ComposeTestRule,
    func: HomeScreenRobot.() -> Unit
) = HomeScreenRobot(composeTestRule).apply { func() }
