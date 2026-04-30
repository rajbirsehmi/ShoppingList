package com.creative.shoppinglist.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput

class ShoppingRobot(private val composeTestRule: ComposeTestRule) {

    fun clickAddFab() {
        composeTestRule.onNodeWithContentDescription("Add Item").performClick()
    }

    fun enterItemName(name: String) {
        composeTestRule.onNodeWithText("What do you need to buy?").performTextInput(name)
    }

    fun toggleImportant() {
        composeTestRule.onNodeWithText("Mark as Important").performClick()
    }

    fun toggleReminder() {
        composeTestRule.onNodeWithText("Set Reminder").performClick()
    }

    fun clickConfirmDate() {
        // Assuming "Next" is the button text in the DatePicker based on Components.kt
        composeTestRule.onNodeWithText("Next").performClick()
    }

    fun clickConfirmTime() {
        // Assuming "OK" is the button text in the TimePicker based on Components.kt
        composeTestRule.onNodeWithText("OK").performClick()
    }

    fun clickAddItemButton() {
        composeTestRule.onNodeWithText("Add to List").performClick()
    }

    fun verifyItemExists(name: String) {
        composeTestRule.onNodeWithText(name).assertIsDisplayed()
    }

    fun clickItem(name: String) {
        composeTestRule.onNodeWithText(name).performClick()
    }

    fun navigateToImportant() {
        composeTestRule.onNodeWithContentDescription("Important").performClick()
    }

    fun navigateToRegular() {
        composeTestRule.onNodeWithContentDescription("Regular").performClick()
    }

    fun verifyEmptyState(message: String) {
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }
}

fun shoppingRobot(composeTestRule: ComposeTestRule, func: ShoppingRobot.() -> Unit) =
    ShoppingRobot(composeTestRule).apply { func() }
