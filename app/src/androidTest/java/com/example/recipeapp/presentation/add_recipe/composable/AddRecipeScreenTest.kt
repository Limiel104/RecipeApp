package com.example.recipeapp.presentation.add_recipe.composable

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.di.AppModule
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.ui.theme.RecipeAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class AddRecipeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() {
                AddRecipeScreen(navController = navController)
            }
        }
    }

    @Test
    fun addRecipeScreen_wholeLayoutSwipesVertically() {
        backButtonIsDisplayed()
        addButtonIsDisplayed()
        servingsSectionIsNotDisplayed()
        prepTimeSectionIsNotDisplayed()

        composeRule
            .onNodeWithTag("Add Recipe Content")
            .performTouchInput { swipeUp() }

        backButtonIsDisplayed()
        addButtonIsDisplayed()
        servingsSectionIsDisplayed()
        prepTimeSectionIsDisplayed()

        composeRule
            .onNodeWithTag("Add Recipe Content")
            .performTouchInput { swipeDown() }

        backButtonIsDisplayed()
        addButtonIsDisplayed()
        servingsSectionIsNotDisplayed()
        prepTimeSectionIsNotDisplayed()
    }

    private fun backButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Back button")
        .assertIsDisplayed()

    private fun addButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Add recipe button")
        .assertIsDisplayed()

    private fun servingsSectionIsDisplayed() = composeRule
        .onNodeWithTag("Servings")
        .assertIsDisplayed()

    private fun servingsSectionIsNotDisplayed() = composeRule
        .onNodeWithTag("Servings")
        .assertIsNotDisplayed()

    private fun prepTimeSectionIsDisplayed() = composeRule
        .onNodeWithTag("Prep time")
        .assertIsDisplayed()

    private fun prepTimeSectionIsNotDisplayed() = composeRule
        .onNodeWithTag("Prep time")
        .assertIsNotDisplayed()
}