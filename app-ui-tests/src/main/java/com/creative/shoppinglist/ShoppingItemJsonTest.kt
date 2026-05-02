package com.creative.shoppinglist

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.creative.shoppinglist.core.testing.TestTags
import com.creative.shoppinglist.core.testing.robots.launchTestingEngine
import com.creative.shoppinglist.domain.repo.ShoppingRepository
import com.creative.shoppinglist.ui.navigation.ShoppingNavGraph
import com.creative.shoppinglist.ui.theme.ShoppingListTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.slf4j.LoggerFactory
import javax.inject.Inject

@Serializable
data class ShoppingItemTestModel(
    val itemName: String,
    val isImportant: Boolean,
)

@HiltAndroidTest
@RunWith(Parameterized::class)
class ShoppingItemJsonTest(private val model: ShoppingItemTestModel) {

    private val logger = LoggerFactory.getLogger(ShoppingItemJsonTest::class.java)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repository: ShoppingRepository

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): List<ShoppingItemTestModel> {
            val context = InstrumentationRegistry.getInstrumentation().context
            val jsonString = context.assets.open("shopping_items.json").bufferedReader().use { it.readText() }
            return Json.decodeFromString(jsonString)
        }
    }

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
    fun addItemFromJson_isDisplayedCorrectly() {
        logger.info("[ENGINE] Starting test with model: {}", model)
        composeTestRule.launchTestingEngine {
            onShoppingScreen {
                openAddItemBottomSheet()
                enterItemName(model.itemName)
                if (model.isImportant) {
                    toggleImportant(isImportant = true)
                }
                saveItem()
                if (model.isImportant) {
                    clickOn(TestTags.BOTTOM_NAV_ITEM + "Important")
                }
                assertItemDisplayed(model.itemName)
            }
        }
        logger.info("[ENGINE] Finished test for: {}", model.itemName)
    }
}
