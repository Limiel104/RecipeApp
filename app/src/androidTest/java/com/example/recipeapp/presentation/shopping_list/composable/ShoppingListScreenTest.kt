package com.example.recipeapp.presentation.shopping_list.composable

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.ui.theme.RecipeAppTheme

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingListScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() {
                ShoppingListScreen(navController = navController)
            }
        }
    }

    @Test
    fun savedRecipesScreen_wholeLayoutSwipesVertically() {
        menuButtonIsDisplayed()
        shoppingListTextIsDisplayed()
        itemsNumberTextIsDisplayed()
        addButtonIsDisplayed()

        composeRule
            .onNodeWithTag("Shopping List Content")
            .performTouchInput { swipeUp() }

        menuButtonIsDisplayed()
        shoppingListTextIsDisplayed()
        itemsNumberTextIsNotDisplayed()
        addButtonIsDisplayed()

        composeRule
            .onNodeWithTag("Shopping List Content")
            .performTouchInput { swipeDown() }

        menuButtonIsDisplayed()
        shoppingListTextIsDisplayed()
        itemsNumberTextIsDisplayed()
        addButtonIsDisplayed()
    }

    private fun menuButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Menu button")
        .assertIsDisplayed()

    private fun shoppingListTextIsDisplayed() = composeRule
        .onNode(hasText("Shopping List"))
        .assertIsDisplayed()

    private fun itemsNumberTextIsDisplayed() = composeRule
        .onNode(hasText(
            text = "items",
            substring = true
        ))
        .assertIsDisplayed()

    private fun itemsNumberTextIsNotDisplayed() = composeRule
        .onNode(hasText("items"))
        .assertIsNotDisplayed()

    private fun addButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Add button")
        .assertIsDisplayed()
}