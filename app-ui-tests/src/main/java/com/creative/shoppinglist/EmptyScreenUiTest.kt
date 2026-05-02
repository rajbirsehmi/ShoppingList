package com.creative.shoppinglist

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.creative.shoppinglist.core.testing.TestTags
import com.creative.shoppinglist.core.testing.robots.launchTestingEngine
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
        composeTestRule.launchTestingEngine {
            onEmptyScreen {
                assertEmptyScreenMainMessageIsDisplayed()
                assertEmptyScreenSubMessageIsDisplayed()
                assertEmptyScreenIconIsDisplayed()
                assertEmptyScreenAddButtonIsDisplayed()
                assertFabIsNotDisplayedWhenItemListIsEmpty()
            }
        }
    }

    @Test
    fun verifyImportantTabEmptyState() {
        composeTestRule.launchTestingEngine {
            onHomeScreen {
                clickOn(TestTags.BOTTOM_NAV_ITEM + "Important")
            }
            onEmptyScreen {
                assertEmptyScreenMainMessageIsDisplayed()
                assertEmptyScreenSubMessageIsDisplayed()
                assertEmptyScreenIconIsDisplayed()
                assertEmptyScreenAddButtonIsDisplayed()
                assertFabIsNotDisplayedWhenItemListIsEmpty()
            }
        }
    }
}
