package com.creative.shoppinglist

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.creative.shoppinglist.core.testing.TestTags
import com.creative.shoppinglist.core.testing.robots.launchTestingEngine
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
        composeTestRule.launchTestingEngine {
            onEmptyScreen {
                assertEmptyScreenMainMessageIsDisplayed()
                assertEmptyScreenAddButtonIsDisplayed()
            }
        }
    }

    @Test
    fun addRegularItem_isDisplayed() {
        val itemName = "Milk"
        composeTestRule.launchTestingEngine {
            onShoppingScreen {
                openAddItemBottomSheet()
                enterItemName(itemName)
                saveItem()
                assertItemDisplayed(itemName)
            }
        }
    }

    @Test
    fun toggleItemCompletion() {
        val itemName = "Eggs"
        composeTestRule.launchTestingEngine {
            onShoppingScreen {
                openAddItemBottomSheet()
                enterItemName(itemName)
                saveItem()
                
                assertItemDisplayed(itemName)
                toggleItemChecked(itemName)
            }
        }
    }

    @Test
    fun deleteItem_removesFromList() {
        val itemName = "Bread"
        composeTestRule.launchTestingEngine {
            onShoppingScreen {
                openAddItemBottomSheet()
                enterItemName(itemName)
                saveItem()
                
                assertItemDisplayed(itemName)
                deleteItemBySwipe(itemName)
                assertItemDoesNotExist(itemName)
            }
        }
    }

    @Test
    fun addImportantItem_appearsInImportantTab() {
        val itemName = "Medicine"
        composeTestRule.launchTestingEngine {
            onShoppingScreen {
                openAddItemBottomSheet()
                enterItemName(itemName)
                toggleImportant(true)
                saveItem()
                
                // Ensure the item is NOT on the regular screen
                assertDoesNotExist(TestTags.SHOPPING_ITEM_CARD_REGULAR + itemName)
                
                waitForIdle()
                clickOn(TestTags.BOTTOM_NAV_ITEM + "Important")
                waitForIdle()

                assertItemDisplayed(itemName)
            }
        }
    }
}
