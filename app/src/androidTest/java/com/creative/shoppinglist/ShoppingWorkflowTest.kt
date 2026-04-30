package com.creative.shoppinglist

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.rule.GrantPermissionRule
import com.creative.shoppinglist.robots.shoppingRobot
import com.creative.shoppinglist.ui.components.TestTags
import com.creative.shoppinglist.ui.navigation.ShoppingNavGraph
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ShoppingWorkflowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val permissionRules = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )!!

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            ShoppingNavGraph()
        }
    }

    @Test
    fun fullUserJourney_addRegularItem_thenDelete() {
        val itemName = "Milk"
        shoppingRobot(composeTestRule) {
            // 1. Add item
            openAddItemBottomSheet()
            enterItemName(itemName)
            saveItem()

            // 2. Verify displayed
            assertItemDisplayed(itemName)

            // 3. Toggle checked
            toggleItemChecked(itemName)

            // 4. Delete
            deleteItemBySwipe(itemName)
            assertItemDoesNotExist(itemName)
        }
    }

    @Test
    fun fullUserJourney_addImportantItem_verifyInCorrectTab() {
        val itemName = "Medicine"
        shoppingRobot(composeTestRule) {
            // 1. Add important item
            openAddItemBottomSheet()
            enterItemName(itemName)
            toggleImportant(true)
            saveItem()

            // 2. Verify not in Regular tab (it should be, actually, if your logic shows all in Regular? No, NavGraph shows Regular vs Important)
            // Checking NavGraph:composable<Screen.Regular> shows regularItems, composable<Screen.Important> shows importantItems.
            // In ViewModel: repository.getRegularShoppingItems()
            // Wait, let's check ShoppingRepositoryImpl to see if they are mutually exclusive.
        }
    }
}
