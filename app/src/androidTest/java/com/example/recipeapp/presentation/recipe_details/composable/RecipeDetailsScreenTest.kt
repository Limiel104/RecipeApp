package com.example.recipeapp.presentation.recipe_details.composable

import androidx.activity.compose.setContent
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.di.AppModule
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.GetRecipeUseCase
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.example.recipeapp.presentation.recipe_details.RecipeDetailsViewModel
import com.example.recipeapp.ui.theme.RecipeAppTheme
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class RecipeDetailsScreenTest {

    private lateinit var recipeWithIngredients: RecipeWithIngredients
    private lateinit var emptyRecipeWithIngredients: RecipeWithIngredients
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getRecipeUseCase: GetRecipeUseCase
    private lateinit var viewModel: RecipeDetailsViewModel

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this)
        savedStateHandle = mockk()
        getRecipeUseCase = mockk()

        recipeWithIngredients = RecipeWithIngredients(
            recipeId = "recipeId",
            name = "Recipe Name",
            ingredients = getIngredientsWithQuantity(),
            prepTime = "40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Category", "Category2", "Category3"),
            date = 1234324354
        )

        emptyRecipeWithIngredients = RecipeWithIngredients(
            recipeId = "",
            name = "",
            ingredients = emptyMap(),
            prepTime = "",
            servings = 0,
            description = "",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "",
            categories = emptyList(),
            date = 0
        )
    }

    private fun setScreen() {
        viewModel = RecipeDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getRecipeUseCase = getRecipeUseCase
        )

        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() { RecipeDetailsScreen(
                navController = navController,
                viewModel = viewModel
            ) }
        }
    }

    private fun setMocks() {
        every { savedStateHandle.get<String>(any()) } returns "recipeId"
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
    }

    private fun verifyMocks() {
        coVerifySequence {
            savedStateHandle.get<String>("recipeId")
            getRecipeUseCase("recipeId")
        }
    }

    @Test
    fun recipeDetailsScreen_allElementsAreVisibleOnLaunch() {
        setMocks()
        setScreen()

        backButtonIsDisplayed()
        bookmarkButtonIsDisplayed()
        nameIsDisplayed()
        ingredientsTabIsDisplayed()
        descriptionTabIsNotDisplayed()

        verifyMocks()
    }

    @Test
    fun ingredientsTab_isVisibleOnLaunch() {
        setMocks()
        setScreen()

        ingredientsTabIsDisplayed()
        descriptionTabIsNotDisplayed()
        lessButtonIsDisplayed()
        composeRule.onNodeWithText("4").assertIsDisplayed()
        moreButtonIsDisplayed()
        composeRule.onNodeWithTag("Ingredients Tab Content").assertIsDisplayed()

        verifyMocks()
    }

    @Test
    fun ingredientsTab_contentIsDisplayedCorrectly() {
        setMocks()
        setScreen()

        ingredientsTabIsDisplayed()
        composeRule.onNodeWithTag("Less button").isDisplayed()
        composeRule.onNodeWithText("2").isDisplayed()
        composeRule.onNodeWithTag("More button").isDisplayed()
        composeRule.onNodeWithTag("Ingredients column").isDisplayed()

        verifyMocks()
    }

    @Test
    fun descriptionTab_isVisibleAfterClick() {
        setMocks()
        setScreen()

        ingredientsTabIsDisplayed()
        descriptionTabIsNotDisplayed()

        composeRule.onNodeWithTag("Description Tab Title").performClick()

        ingredientsTabIsNotDisplayed()
        descriptionTabIsDisplayed()

        verifyMocks()
    }

    @Test
    fun descriptionTab_descriptionIsDisplayed() {
        setMocks()
        setScreen()

        composeRule.onNodeWithTag("Description Tab Title").performClick()
        descriptionTabIsDisplayed()
        composeRule.onNodeWithTag("Description Tab Content")
            .assert(hasText(recipeWithIngredients.description))

        verifyMocks()
    }

    @Test
    fun ingredientsColumn_displaysCorrectNumberOfIngredients() {
        setMocks()
        setScreen()

        composeRule.onNodeWithTag("Ingredients column").isDisplayed()
        var numberOfIngredients = 0
        val children = composeRule.onNodeWithTag("Ingredients column")
            .fetchSemanticsNode()
            .children

        for(child in children) {
            val testTag = child.config.getOrElse(SemanticsProperties.TestTag) { "" }
            if(testTag.contains("Ingredient Item"))
                numberOfIngredients += 1
        }

        verifyMocks()
        Truth.assertThat(numberOfIngredients).isEqualTo(3)
    }

    @Test
    fun ingredientsColumn_ingredientsNamesAreDisplayedCorrectly() {
        setMocks()
        setScreen()

        val children = composeRule.onNodeWithTag("Ingredients column")
            .fetchSemanticsNode()
            .children

        for(i in 0..2) {
            val text = children[i].config.getOrElse(SemanticsProperties.Text) { emptyList() }
            if(text.isNotEmpty()) {
                val name = recipeWithIngredients.ingredients.keys.toList()[i]
                Truth.assertThat(text).isEqualTo(name)
            }
        }

        verifyMocks()
    }

    @Test
    fun ingredientsColumn_afterIncrease_quantityIsDisplayedCorrectly() {
        setMocks()
        setScreen()

        composeRule.onNodeWithText(" Ingredient Name 200.0 g").isDisplayed()
        composeRule.onNodeWithText(" Ingredient2 Name 5.0 kg").isDisplayed()
        composeRule.onNodeWithText(" Ingredient3 Name 1 cup").isDisplayed()

        composeRule.onNodeWithContentDescription("Less button").performClick()

        composeRule.onNodeWithText(" Ingredient Name 150.0 g").isDisplayed()
        composeRule.onNodeWithText(" Ingredient2 Name 3.75 kg").isDisplayed()
        composeRule.onNodeWithText(" Ingredient3 Name 0.75 cup").isDisplayed()

        verifyMocks()
    }

    @Test
    fun ingredientsColumn_afterDecrease_quantityIsDisplayedCorrectly() {
        setMocks()
        setScreen()

        composeRule.onNodeWithText(" Ingredient Name 200.0 g").isDisplayed()
        composeRule.onNodeWithText(" Ingredient2 Name 5.0 kg").isDisplayed()
        composeRule.onNodeWithText(" Ingredient3 Name 1 cup").isDisplayed()

        composeRule.onNodeWithContentDescription("More button").performClick()

        composeRule.onNodeWithText(" Ingredient Name 250.0 g").isDisplayed()
        composeRule.onNodeWithText(" Ingredient2 Name 6.25 kg").isDisplayed()
        composeRule.onNodeWithText(" Ingredient3 Name 1.25 cup").isDisplayed()

        verifyMocks()
    }

    @Test
    fun servings_cannotBeLessThanOne() {
        setMocks()
        setScreen()

        composeRule.onNodeWithText("4").isDisplayed()
        for(i in 0..5)
            composeRule.onNodeWithContentDescription("Less button").performClick()
        composeRule.onNodeWithText("1").isDisplayed()

        verifyMocks()
    }

    @Test
    fun servings_cannotBeMoreThanTwenty() {
        setMocks()
        setScreen()

        composeRule.onNodeWithText("4").isDisplayed()
        for(i in 0..18)
            composeRule.onNodeWithContentDescription("More button").performClick()
        composeRule.onNodeWithText("20").isDisplayed()

        verifyMocks()
    }

    @Test
    fun backButton_homeScreenIsDisplayed() {
        setMocks()
        setScreen()

        composeRule.onNodeWithTag("Recipe Details Content").isNotDisplayed()
        composeRule.onNodeWithTag("Home Content").isNotDisplayed()

        backButtonIsDisplayed()
        composeRule.onNodeWithContentDescription("Back button").performClick()

        composeRule.onNodeWithTag("Recipe Details Content").isNotDisplayed()
        composeRule.onNodeWithTag("Home Content").isDisplayed()

        verifyMocks()
    }

    @Test
    fun recipeScreen_emptyIngredientList_ingredientsListIsNotDisplayed() {
        every { savedStateHandle.get<String>(any()) } returns "recipeId"
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(emptyRecipeWithIngredients))
        setScreen()

        backButtonIsDisplayed()
        bookmarkButtonIsDisplayed()
        lessButtonIsNotDisplayed()
        moreButtonIsNotDisplayed()
        composeRule.onNodeWithTag("Ingredients Tab Content").assertIsNotDisplayed()
        composeRule.onNodeWithText("4").assertIsNotDisplayed()
        val ingredientsNumber = composeRule.onNodeWithTag("Ingredients column")
            .fetchSemanticsNode()
            .children
            .size

        verifyMocks()
        Truth.assertThat(ingredientsNumber).isEqualTo(0)
    }

    private fun backButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Back button")
        .assertIsDisplayed()

    private fun bookmarkButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Bookmark recipe button")
        .assertIsDisplayed()

    private fun nameIsDisplayed() = composeRule
        .onNodeWithText("Recipe Name")
        .assertIsDisplayed()

    private fun ingredientsTabIsDisplayed() = composeRule
        .onNodeWithTag("Ingredients Tab Content")
        .assertIsDisplayed()

    private fun ingredientsTabIsNotDisplayed() = composeRule
        .onNodeWithTag("Ingredients Tab Content")
        .assertIsNotDisplayed()

    private fun lessButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Less button")
        .assertIsDisplayed()

    private fun lessButtonIsNotDisplayed() = composeRule
        .onNodeWithContentDescription("Less button")
        .assertIsNotDisplayed()

    private fun moreButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("More button")
        .assertIsDisplayed()

    private fun moreButtonIsNotDisplayed() = composeRule
        .onNodeWithContentDescription("More button")
        .assertIsNotDisplayed()

    private fun descriptionTabIsDisplayed() = composeRule
        .onNodeWithTag("Description Tab Content")
        .assertIsDisplayed()

    private fun descriptionTabIsNotDisplayed() = composeRule
        .onNodeWithTag("Description Tab Content")
        .assertIsNotDisplayed()
}