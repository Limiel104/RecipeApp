package com.example.recipeapp.presentation.account.composable

import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.di.AppModule
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.presentation.account.AccountState
import com.example.recipeapp.ui.theme.RecipeAppTheme
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class AccountScreenTest {

    private lateinit var recipe: Recipe
    private lateinit var recipe2: Recipe
    private lateinit var recipe3: Recipe
    private lateinit var recipe4: Recipe
    private lateinit var recipe5: Recipe
    private lateinit var recipe6: Recipe
    private lateinit var recipe7: Recipe
    private lateinit var recipes: List<Recipe>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        recipe = Recipe(
            recipeId = "recipeId",
            name = "Recipe Name",
            prepTime = "4 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "userId",
            categories = listOf("Dinner"),
            date = 12345623423
        )

        recipe2 = Recipe(
            recipeId = "recipe2Id",
            name = "Recipe2 Name",
            prepTime = "55 min",
            servings = 6,
            description = "Recipe2 description",
            isVegetarian = true,
            isVegan = true,
            imageUrl = "",
            createdBy = "userId",
            categories = listOf("Pizza","Dinner"),
            date = 12345623423
        )

        recipe3 = Recipe(
            recipeId = "recipe3Id",
            name = "Recipe3 Name",
            prepTime = "4,5 h",
            servings = 1,
            description = "Recipe3 description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "user3Id",
            categories = listOf("Dinner"),
            date = 12345623423
        )

        recipe4 = Recipe(
            recipeId = "recipe4Id",
            name = "Recipe4 Name",
            prepTime = "1h",
            servings = 4,
            description = "Recipe4 description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "",
            createdBy = "user2Id",
            categories = listOf("Stew"),
            date = 12345623423
        )

        recipe5 = Recipe(
            recipeId = "recipe5Id",
            name = "Recipe5 Name",
            prepTime = "45 min",
            servings = 6,
            description = "Recipe5 description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "userId",
            categories = listOf("Appetizer","Fish"),
            date = 12345623423
        )

        recipe6 = Recipe(
            recipeId = "recipe6Id",
            name = "Recipe6 Name",
            prepTime = "30 min",
            servings = 3,
            description = "Recipe6 description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "userId",
            categories = listOf("Dinner","Pork","Stew"),
            date = 12345623423
        )

        recipe7 = Recipe(
            recipeId = "recipe7Id",
            name = "Recipe7 Name",
            prepTime = "13 min",
            servings = 5,
            description = "Recipe7 description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "user2Id",
            categories = listOf("Appetizer"),
            date = 12345623423
        )

        recipes = listOf(recipe, recipe2, recipe3, recipe4, recipe5, recipe6, recipe7).shuffled()
    }

    private fun setScreen() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() { AccountScreen(navController = navController) }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun setScreenState(
        uiState: AccountState = AccountState()
    ) {
        composeRule.activity.setContent {
            RecipeAppTheme {
                AccountContent(
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                        rememberTopAppBarState()
                    ),
                    uiState = uiState,
                    onAddRecipe = {},
                    onRecipeSelected = {},
                    onLogout = {},
                    onSortRecipes = {},
                    onEditButtonClicked = {},
                    onNameChange = {},
                    onPasswordChange = {},
                    onConfirmPasswordChange = {},
                    onDialogDismiss = {},
                    onDialogSave = {}
                )
            }
        }
    }

    @Test
    fun accountScreen_nameIsDisplayedCorrectly() {
        setScreenState(AccountState(name = "User Name"))
        userNameTextIsDisplayed()
    }

    @Test
    fun accountScreen_sortTextButtonIsClickable() {
        setScreenState()
        sortTextButtonIsDisplayed()
        composeRule.onNodeWithText("Newest").assertHasClickAction()
    }

    @Test
    fun accountScreen_addButtonIsClickable() {
        setScreenState()
        addButtonIsDisplayed()
        composeRule.onNodeWithContentDescription("Add recipe").assertHasClickAction()
    }

    @Test
    fun accountScreen_editDialogIsOpened() {
        setScreenState(AccountState(isEditDialogActivated = true))
        composeRule.onNodeWithTag("Edit dialog").assertIsDisplayed()
    }

    @Test
    fun accountScreen_editDialogIsDisplayedCorrectly() {
        setScreenState(AccountState(isEditDialogActivated = true))

        composeRule.onNodeWithText("Edit Account").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Clear button").assertIsDisplayed()
        composeRule.onNodeWithTag("Save button").assertIsDisplayed()
        composeRule.onNodeWithTag("Save button").assert(hasText("Save"))
        composeRule.onNodeWithTag("Account name TF").assertIsDisplayed()
        composeRule.onNodeWithTag("Account password TF").assertIsDisplayed()
        composeRule.onNodeWithTag("Account confirm password TF").assertIsDisplayed()
    }

    @Test
    fun accountScreen_passwordTextField_inputTextIsDisplayedCorrectly() {
        val password = "Qwerty1+"
        setScreenState(
            AccountState(
                isEditDialogActivated = true,
                password = password
        ))

        composeRule.onNodeWithTag("Account password TF").performTextInput(password)
        val passwordNode = composeRule.onNodeWithTag("Account password TF").fetchSemanticsNode()
        val textInput = passwordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(password)
    }

    @Test
    fun accountScreen_confirmPasswordTextField_inputTextIsDisplayedCorrectly() {
        val confirmPassword = "Qwerty1+"
        setScreenState(
            AccountState(
                isEditDialogActivated = true,
                confirmPassword = confirmPassword
        ))

        composeRule.onNodeWithTag("Account confirm password TF").performTextInput(confirmPassword)
        val confirmPasswordNode = composeRule.onNodeWithTag("Account confirm password TF").fetchSemanticsNode()
        val textInput = confirmPasswordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(confirmPassword)
    }

    @Test
    fun accountScreen_nameTextField_inputTextIsDisplayedCorrectly() {
        val name = "John Smith"
        setScreenState(
            AccountState(
                isEditDialogActivated = true,
                editName = name
        ))

        composeRule.onNodeWithTag("Account name TF").performTextInput(name)
        val confirmPasswordNode = composeRule.onNodeWithTag("Account name TF").fetchSemanticsNode()
        val textInput = confirmPasswordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(name)
    }

    @Test
    fun accountScreen_passwordErrorTextField_errorIsDisplayedCorrectly() {
        setScreenState(
            AccountState(
            isEditDialogActivated = true,
            passwordError = "Password can't be empty"
        ))

        val passwordNode = composeRule.onNodeWithTag("Account password TF").fetchSemanticsNode()
        val errorLabel = passwordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val errorValue = passwordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()
        assertThat(errorLabel).isEqualTo("Password")
        assertThat(errorValue).isEqualTo("Password can't be empty")
    }

    @Test
    fun accountScreen_confirmPasswordErrorTextField_errorIsDisplayedCorrectly() {
        setScreenState(
            AccountState(
                isEditDialogActivated = true,
                confirmPasswordError = "Passwords don't mach"
        ))

        val confirmPasswordNode = composeRule.onNodeWithTag("Account confirm password TF").fetchSemanticsNode()
        val errorLabel = confirmPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val errorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()
        assertThat(errorLabel).isEqualTo("Confirm Password")
        assertThat(errorValue).isEqualTo("Passwords don't mach")
    }

    @Test
    fun accountScreen_nameErrorTextField_errorIsDisplayedCorrectly() {
        setScreenState(
            AccountState(
                isEditDialogActivated = true,
                nameError = "Name can't be empty"
        ))

        val nameNode = composeRule.onNodeWithTag("Account name TF").fetchSemanticsNode()
        val errorLabel = nameNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val errorValue = nameNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()
        assertThat(errorLabel).isEqualTo("Name")
        assertThat(errorValue).isEqualTo("Name can't be empty")
    }

    @Test
    fun accountScreen_wholeLayoutSwipesVertically() {
        setScreenState(
            AccountState(
                name = "User Name",
                recipes = recipes
        ))

        backButtonIsDisplayed()
        editButtonIsDisplayed()
        logoutButtonIsDisplayed()
        userNameTextIsDisplayed()
        recipesTextIsDisplayed()
        sortTextButtonIsDisplayed()
        addButtonIsDisplayed()

        composeRule
            .onNodeWithTag("Account Content")
            .performTouchInput { swipeUp() }

        backButtonIsDisplayed()
        editButtonIsDisplayed()
        logoutButtonIsDisplayed()
        userNameTextIsDisplayed()
        recipesTextIsNotDisplayed()
        sortTextButtonIsNotDisplayed()
        addButtonIsDisplayed()

        composeRule
            .onNodeWithTag("Account Content")
            .performTouchInput { swipeDown() }

        backButtonIsDisplayed()
        editButtonIsDisplayed()
        logoutButtonIsDisplayed()
        userNameTextIsDisplayed()
        recipesTextIsDisplayed()
        sortTextButtonIsDisplayed()
        addButtonIsDisplayed()
    }

    private fun backButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Back button")
        .assertIsDisplayed()

    private fun editButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Edit button")
        .assertIsDisplayed()

    private fun logoutButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Logout button")
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

    private fun sortTextButtonIsDisplayed() = composeRule
        .onNode(hasText("Newest"))
        .assertIsDisplayed()

    private fun sortTextButtonIsNotDisplayed() = composeRule
        .onNode(hasText("Newest"))
        .assertIsNotDisplayed()

    private fun addButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Add recipe")
        .assertIsDisplayed()
}