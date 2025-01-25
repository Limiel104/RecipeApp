package com.example.recipeapp.presentation.saved_recipes.composable

import androidx.activity.compose.setContent
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.di.AppModule
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.AddSearchSuggestionUseCase
import com.example.recipeapp.domain.use_case.DeleteSavedRecipeUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetSavedRecipeIdUseCase
import com.example.recipeapp.domain.use_case.GetSearchSuggestionsUseCase
import com.example.recipeapp.domain.use_case.GetUserSavedRecipesUseCase
import com.example.recipeapp.domain.use_case.SortRecipesUseCase
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.presentation.common.getRecipes
import com.example.recipeapp.presentation.common.getSearchSuggestions
import com.example.recipeapp.presentation.saved_recipes.SavedRecipesViewModel
import com.example.recipeapp.ui.theme.RecipeAppTheme
import com.google.common.truth.Truth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SavedRecipesScreenTest {

    private lateinit var deleteSavedRecipeUseCase: DeleteSavedRecipeUseCase
    private lateinit var getUserSavedRecipesUseCase: GetUserSavedRecipesUseCase
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var getSavedRecipeIdUseCase: GetSavedRecipeIdUseCase
    private lateinit var addSearchSuggestionUseCase: AddSearchSuggestionUseCase
    private lateinit var getSearchSuggestionUseCase: GetSearchSuggestionsUseCase
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var viewModel: SavedRecipesViewModel

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this)
        deleteSavedRecipeUseCase = mockk()
        getUserSavedRecipesUseCase = mockk()
        getCurrentUserUseCase = mockk()
        getSavedRecipeIdUseCase = mockk()
        addSearchSuggestionUseCase = mockk()
        getSearchSuggestionUseCase = mockk()
        firebaseUser = mockk()
    }

    @After
    fun tearDown() {
        confirmVerified(deleteSavedRecipeUseCase)
        confirmVerified(getUserSavedRecipesUseCase)
        confirmVerified(getCurrentUserUseCase)
        confirmVerified(getSavedRecipeIdUseCase)
        confirmVerified(addSearchSuggestionUseCase)
        confirmVerified(getSearchSuggestionUseCase)
        confirmVerified(firebaseUser)
        clearAllMocks()
    }

    private fun setScreen() {
        viewModel = SavedRecipesViewModel(
            getCurrentUserUseCase = getCurrentUserUseCase,
            getUserSavedRecipesUseCase = getUserSavedRecipesUseCase,
            deleteSavedRecipeUseCase = deleteSavedRecipeUseCase,
            getSavedRecipeIdUseCase = getSavedRecipeIdUseCase,
            addSearchSuggestionUseCase = addSearchSuggestionUseCase,
            getSearchSuggestionsUseCase = getSearchSuggestionUseCase,
            sortRecipesUseCase = SortRecipesUseCase()
        )

        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() {
                SavedRecipesScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }

    private fun setMocks() {
        every { getCurrentUserUseCase() } returns firebaseUser
        every { firebaseUser.uid } returns "userUID"
        coEvery {
            getUserSavedRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(getRecipes()))
    }

    private fun verifyMocks() {
        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
            firebaseUser.uid
            getUserSavedRecipesUseCase(any(), any(), any())
        }
    }

    @Test
    fun savedRecipesScreen_allElementsAreVisibleOnLaunch() {
        setMocks()
        setScreen()

        searchBarIsDisplayed()
        recipesTextIsDisplayed()
        newestTextButtonIsDisplayed()
        savedRecipesLazyColumnIsDisplayed()

        verifyMocks()
    }

    @Test
    fun searchBar_isClickable() {
        setMocks()
        coEvery { getSearchSuggestionUseCase() } returns flowOf(
            Resource.Success(
                getSearchSuggestions()
            )
        )
        setScreen()

        searchBarIsDisplayed()
        composeRule.onNodeWithTag("Search Bar").performClick()

        verifyMocks()
        coVerify { getSearchSuggestionUseCase() }
    }

    @Test
    fun sortTextButton_IsClickable() {
        setMocks()
        setScreen()

        newestTextButtonIsDisplayed()
        composeRule.onNodeWithText("Newest").assertHasClickAction()

        verifyMocks()
    }

    @Test
    fun sortTextButton_changesAfterClick() {
        setMocks()
        setScreen()

        newestTextButtonIsDisplayed()
        oldestTextButtonIsNotDisplayed()

        composeRule.onNodeWithText("Newest").performClick()

        newestTextButtonIsNotDisplayed()
        oldestTextButtonIsDisplayed()

        composeRule.onNodeWithText("Oldest").performClick()

        newestTextButtonIsDisplayed()
        oldestTextButtonIsNotDisplayed()

        verifyMocks()
    }

    @Test
    fun savedRecipesLazyColumn_displaysCorrectNumberOfRecipes() {
        setMocks()
        setScreen()

        savedRecipesLazyColumnIsDisplayed()

        var numberOfRecipes = 0
        val children = savedRecipesLazyColumnChildren()

        for(child in children) {
            val testTag = child.config.getOrElse(SemanticsProperties.TestTag) { "" }
            if(testTag.contains("Recipe "))
                numberOfRecipes += 1
        }

        verifyMocks()
        Truth.assertThat(numberOfRecipes).isEqualTo(6)
    }

    @Test
    fun savedRecipesLazyColumn_displaysCorrectNames() {
        setMocks()
        setScreen()

        val children = savedRecipesLazyColumnChildren()

        for(child in children) {
            val testTag = child.config.getOrElse(SemanticsProperties.TestTag) { "" }
            if(testTag.contains("Name")) {
                composeRule.onNodeWithTag(testTag).assert(hasText(testTag.substring(7)))
            }
        }

        verifyMocks()
    }

    private fun searchBarIsDisplayed() = composeRule
        .onNodeWithTag("Search Bar")
        .assertIsDisplayed()

    private fun searchBarIsNotDisplayed() = composeRule
        .onNodeWithTag("Search Bar")
        .assertIsNotDisplayed()

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

    private fun oldestTextButtonIsDisplayed() = composeRule
        .onNode(hasText("Oldest"))
        .assertIsDisplayed()

    private fun oldestTextButtonIsNotDisplayed() = composeRule
        .onNode(hasText("Oldest"))
        .assertIsNotDisplayed()

    private fun savedRecipesLazyColumnIsDisplayed() = composeRule
        .onNodeWithTag("Saved Recipes Lazy Column")
        .assertIsDisplayed()

    private fun savedRecipesLazyColumnChildren() = composeRule
        .onNodeWithTag("Saved Recipes Lazy Column")
        .fetchSemanticsNode()
        .children
}