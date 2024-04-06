package com.example.recipeapp.presentation.home.composable

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.ui.theme.RecipeAppTheme

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        //Launch the Home screen
        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() {
                HomeScreen(navController = navController)

            }
        }
    }

    @Test
    fun searchBar_homeScreen_isClickable() {
        searchBarIsDisplayed()
        composeRule.onNodeWithTag("Search Bar").performClick()
    }

    @Test
    fun topCategoriesSection_homeScreen_swipesLeftAndRight() {
        val firstCategoryName = "TCS Category 1"
        val lastCategoryName = "TCS Category 8"

        composeRule
            .onNodeWithTag("Top Categories Section")
            .assertIsDisplayed()

        composeRule.onNodeWithTag(firstCategoryName).assertIsDisplayed()
        composeRule.onNodeWithTag(lastCategoryName).assertIsNotDisplayed()

        composeRule.onNodeWithTag("Top Categories Section").performTouchInput { swipeLeft() }

        composeRule.onNodeWithTag(firstCategoryName).assertIsNotDisplayed()
        composeRule.onNodeWithTag(lastCategoryName).assertIsDisplayed()

        composeRule.onNodeWithTag("Top Categories Section").performTouchInput { swipeRight() }

        composeRule.onNodeWithTag(firstCategoryName).assertIsDisplayed()
        composeRule.onNodeWithTag(lastCategoryName).assertIsNotDisplayed()
    }

    @Test
    fun homeScreen_allSectionsSwipeVertically() {
        searchBarIsDisplayed()
        topCategoriesSectionIsDisplayed()
        recipesTextIsDisplayed()

        composeRule
            .onNodeWithTag("Home Content")
            .performTouchInput { swipeUp() }

        searchBarIsNotDisplayed()
        topCategoriesSectionIsNotDisplayed()
        recipesTextIsNotDisplayed()

        composeRule
            .onNodeWithTag("Home Content")
            .performTouchInput { swipeDown() }

        searchBarIsDisplayed()
        topCategoriesSectionIsDisplayed()
        recipesTextIsDisplayed()
    }



    private fun searchBarIsDisplayed() = composeRule
        .onNodeWithTag("Search Bar")
        .assertIsDisplayed()

    private fun searchBarIsNotDisplayed() = composeRule
        .onNodeWithTag("Search Bar")
        .assertIsNotDisplayed()

    private fun topCategoriesSectionIsDisplayed() = composeRule
        .onNodeWithTag("Top Categories Section")
        .assertIsDisplayed()

    private fun topCategoriesSectionIsNotDisplayed() = composeRule
        .onNodeWithTag("Top Categories Section")
        .assertIsNotDisplayed()

    private fun recipesTextIsDisplayed() = composeRule
        .onNode(hasText("Recipes"))
        .assertIsDisplayed()

    private fun recipesTextIsNotDisplayed() = composeRule
        .onNode(hasText("Recipes"))
        .assertIsNotDisplayed()
}