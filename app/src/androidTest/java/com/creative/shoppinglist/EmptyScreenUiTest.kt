package com.creative.shoppinglist

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.creative.shoppinglist.robots.emptyScreenRobot
import com.creative.shoppinglist.ui.components.TestTags
import com.creative.shoppinglist.ui.navigation.ShoppingNavGraph
import com.creative.shoppinglist.ui.theme.ShoppingListTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class EmptyScreenUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
//    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            ShoppingListTheme {
                ShoppingNavGraph()
            }
        }
    }

    @Test
    fun verifyRegularTabEmptyState() {
        emptyScreenRobot(composeTestRule) {
            assertEmptyScreenMainMessageIsDisplayed()
            assertEmptyScreenSubMessageIsDisplayed()
            assertEmptyScreenIconIsDisplayed()
            assertEmptyScreenAddButtonIsDisplayed()
            assertFabIsNotDisplayedWhenItemListIsEmpty()
        }
    }

    @Test
    fun verifyImportantTabEmptyState() {
        emptyScreenRobot(composeTestRule) {
            // Switch to Important tab
            clickOn(TestTags.BOTTOM_NAV_ITEM + "Important")

            assertEmptyScreenMainMessageIsDisplayed()
            assertEmptyScreenSubMessageIsDisplayed()
            assertEmptyScreenIconIsDisplayed()
            assertEmptyScreenAddButtonIsDisplayed()
            assertFabIsNotDisplayedWhenItemListIsEmpty()
        }
    }
}
