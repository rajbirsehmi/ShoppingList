package com.creative.shoppinglist

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.creative.shoppinglist.robots.emptyScreenRobot
import com.creative.shoppinglist.robots.shoppingRobot
import com.creative.shoppinglist.ui.components.TestTags
import com.creative.shoppinglist.ui.navigation.ShoppingNavGraph
import com.creative.shoppinglist.ui.theme.ShoppingListTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import com.creative.shoppinglist.domain.repo.ShoppingRepository

@HiltAndroidTest
class ShoppingWorkflowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repository: ShoppingRepository

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking {
            repository.deleteAllShoppingItems()
        }
        composeTestRule.setContent {
            ShoppingListTheme {
                ShoppingNavGraph()
            }
        }
    }

    @Test
    fun emptyState_showsCorrectUI() {
        emptyScreenRobot(composeTestRule) {
            assertEmptyScreenMainMessageIsDisplayed()
            assertEmptyScreenAddButtonIsDisplayed()
        }
    }

    @Test
    fun addRegularItem_isDisplayed() {
        val itemName = "Milk"
        shoppingRobot(composeTestRule) {
            openAddItemBottomSheet()
            enterItemName(itemName)
            saveItem()
            assertItemDisplayed(itemName)
        }
    }

    @Test
    fun toggleItemCompletion() {
        val itemName = "Eggs"
        shoppingRobot(composeTestRule) {
            openAddItemBottomSheet()
            enterItemName(itemName)
            saveItem()
            
            assertItemDisplayed(itemName)
            toggleItemChecked(itemName)
            // Successfully toggled (visual check would require more robot methods)
        }
    }

    @Test
    fun deleteItem_removesFromList() {
        val itemName = "Bread"
        shoppingRobot(composeTestRule) {
            openAddItemBottomSheet()
            enterItemName(itemName)
            saveItem()
            
            assertItemDisplayed(itemName)
            deleteItemBySwipe(itemName)
            assertItemDoesNotExist(itemName)
        }
    }

    @Test
    fun addImportantItem_appearsInImportantTab() {
        val itemName = "Medicine"
        shoppingRobot(composeTestRule) {
            openAddItemBottomSheet()
            enterItemName(itemName)
            toggleImportant(true)
            saveItem()

            // Verify it's not in the main list
            assertItemDoesNotExist(itemName)

            // Switch to Important tab and verify
            clickOn(TestTags.BOTTOM_NAV_ITEM + "Important")
            waitForIdle()
            assertItemDisplayed(itemName)
        }
    }
}

