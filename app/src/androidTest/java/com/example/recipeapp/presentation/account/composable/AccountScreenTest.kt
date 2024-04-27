package com.example.recipeapp.presentation.account.composable

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
class AccountScreenTest {

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
                AccountScreen(navController = navController)
            }
        }
    }

    @Test
    fun accountScreen_wholeLayoutSwipesVertically() {
        backButtonIsDisplayed()
        editButtonIsDisplayed()
        userNameTextIsDisplayed()
        recipesTextIsDisplayed()
        newestTextButtonIsDisplayed()

        composeRule
            .onNodeWithTag("Account Content")
            .performTouchInput { swipeUp() }

        backButtonIsDisplayed()
        editButtonIsDisplayed()
        userNameTextIsDisplayed()
//        recipesTextIsNotDisplayed()
//        newestTextButtonIsNotDisplayed()

        composeRule
            .onNodeWithTag("Account Content")
            .performTouchInput { swipeDown() }

        backButtonIsDisplayed()
        editButtonIsDisplayed()
        userNameTextIsDisplayed()
        recipesTextIsDisplayed()
        newestTextButtonIsDisplayed()
    }

    private fun backButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Back button")
        .assertIsDisplayed()

    private fun editButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Edit button")
        .assertIsDisplayed()

    private fun userNameTextIsDisplayed() = composeRule
        .onNode(hasText("User Name"))
        .assertIsDisplayed()

    private fun recipesTextIsDisplayed() = composeRule
        .onNode(hasText("Recipes"))
        .assertIsDisplayed()

    private fun recipesTextIsNotDisplayed() = composeRule
        .onNode(hasText("Recipes"))
        .assertIsNotDisplayed()

    private fun newestTextButtonIsDisplayed() = composeRule
        .onNode(hasText("Newest"))
        .assertIsDisplayed()

    private fun newestTextButtonIsNotDisplayed() = composeRule
        .onNode(hasText("Newest"))
        .assertIsNotDisplayed()
}