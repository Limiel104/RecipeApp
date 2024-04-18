package com.example.recipeapp.presentation.recipe_details.composable

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
class RecipeDetailsScreenTest {

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
                RecipeDetailsScreen(navController = navController)
            }
        }
    }

    @Test
    fun ingredientsTab_recipeDetailsScreen_isVisibleOnLaunch() {
        ingredientsTabIsDisplayed()
        descriptionTabIsNotDisplayed()
    }

    @Test
    fun descriptionTab_recipeDetailsScreen_isVisibleAfterClick() {
        ingredientsTabIsDisplayed()
        descriptionTabIsNotDisplayed()

        composeRule
            .onNodeWithTag("Description Tab Title").performClick()

        ingredientsTabIsNotDisplayed()
        descriptionTabIsDisplayed()
    }

    private fun ingredientsTabIsDisplayed() = composeRule
        .onNodeWithTag("Ingredients Tab Content")
        .assertIsDisplayed()

    private fun ingredientsTabIsNotDisplayed() = composeRule
        .onNodeWithTag("Ingredients Tab Content")
        .assertIsNotDisplayed()

    private fun descriptionTabIsDisplayed() = composeRule
        .onNodeWithTag("Description Tab Content")
        .assertIsDisplayed()

    private fun descriptionTabIsNotDisplayed() = composeRule
        .onNodeWithTag("Description Tab Content")
        .assertIsNotDisplayed()
}