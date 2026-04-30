package com.creative.shoppinglist

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.creative.shoppinglist.ui.navigation.ShoppingNavGraph
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ShoppingUITest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun setup() {
        composeTestRule.setContent {
            ShoppingNavGraph()
        }
    }
}
