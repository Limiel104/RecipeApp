package com.example.recipeapp.presentation.end_to_end

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipeapp.di.AppModule
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.AddSavedRecipeUseCase
import com.example.recipeapp.domain.use_case.AddSearchSuggestionUseCase
import com.example.recipeapp.domain.use_case.DeleteSavedRecipeUseCase
import com.example.recipeapp.domain.use_case.GetCategoriesUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.domain.use_case.GetRecipeUseCase
import com.example.recipeapp.domain.use_case.GetRecipesUseCase
import com.example.recipeapp.domain.use_case.GetSavedRecipeIdUseCase
import com.example.recipeapp.domain.use_case.GetSearchSuggestionsUseCase
import com.example.recipeapp.domain.use_case.GetUserSavedRecipesUseCase
import com.example.recipeapp.domain.use_case.GetUserShoppingListsUseCase
import com.example.recipeapp.domain.use_case.SortRecipesUseCase
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.presentation.common.getCategories
import com.example.recipeapp.presentation.common.getIngredients
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.example.recipeapp.presentation.common.getRecipes
import com.example.recipeapp.presentation.common.getSearchSuggestions
import com.example.recipeapp.presentation.common.getShoppingLists
import com.example.recipeapp.presentation.home.HomeViewModel
import com.example.recipeapp.presentation.home.composable.HomeScreen
import com.example.recipeapp.presentation.navigation.Screen
import com.example.recipeapp.presentation.recipe_details.RecipeDetailsViewModel
import com.example.recipeapp.presentation.recipe_details.composable.RecipeDetailsScreen
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
class RecipeEnd2EndTest {

    private lateinit var getIngredientsUseCase: GetIngredientsUseCase
    private lateinit var getRecipesUseCase: GetRecipesUseCase
    private lateinit var getUserShoppingListsUseCase: GetUserShoppingListsUseCase
    private lateinit var addSearchSuggestionUseCase: AddSearchSuggestionUseCase
    private lateinit var getSearchSuggestionsUseCase: GetSearchSuggestionsUseCase
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var recipeWithIngredients: RecipeWithIngredients
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getRecipeUseCase: GetRecipeUseCase
    private lateinit var addSavedRecipeUseCase: AddSavedRecipeUseCase
    private lateinit var deleteSavedRecipeUseCase: DeleteSavedRecipeUseCase
    private lateinit var getUserSavedRecipesUseCase: GetUserSavedRecipesUseCase
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var getSavedRecipeIdUseCase: GetSavedRecipeIdUseCase
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var recipeDetailsViewModel: RecipeDetailsViewModel

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this)

        getIngredientsUseCase = mockk()
        getRecipesUseCase = mockk()
        getUserShoppingListsUseCase = mockk()
        addSearchSuggestionUseCase = mockk()
        getSearchSuggestionsUseCase = mockk()
        getCategoriesUseCase = mockk()

        savedStateHandle = mockk()
        getRecipeUseCase = mockk()
        addSavedRecipeUseCase = mockk()
        deleteSavedRecipeUseCase = mockk()
        getUserSavedRecipesUseCase = mockk()
        getCurrentUserUseCase = mockk()
        getSavedRecipeIdUseCase = mockk()
        firebaseUser = mockk()

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
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun setOnlyHomeScreen() {
        homeViewModel = HomeViewModel(
            getIngredientsUseCase = getIngredientsUseCase,
            getRecipesUseCase = getRecipesUseCase,
            getUserShoppingListsUseCase = getUserShoppingListsUseCase,
            addSearchSuggestionUseCase = addSearchSuggestionUseCase,
            getSearchSuggestionsUseCase = getSearchSuggestionsUseCase,
            getCategoriesUseCase = getCategoriesUseCase,
            sortRecipesUseCase = SortRecipesUseCase()
        )

        composeRule.activity.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.route
            ) {
                composable(
                    route = Screen.HomeScreen.route
                ) {
                    HomeScreen(
                        navController = navController,
                        viewModel = homeViewModel
                    )
                }
            }
        }
    }

    private fun setScreen() {
        homeViewModel = HomeViewModel(
            getIngredientsUseCase = getIngredientsUseCase,
            getRecipesUseCase = getRecipesUseCase,
            getUserShoppingListsUseCase = getUserShoppingListsUseCase,
            addSearchSuggestionUseCase = addSearchSuggestionUseCase,
            getSearchSuggestionsUseCase = getSearchSuggestionsUseCase,
            getCategoriesUseCase = getCategoriesUseCase,
            sortRecipesUseCase = SortRecipesUseCase()
        )

        recipeDetailsViewModel = RecipeDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getRecipeUseCase = getRecipeUseCase,
            addSavedRecipeUseCase = addSavedRecipeUseCase,
            deleteSavedRecipeUseCase = deleteSavedRecipeUseCase,
            getUserSavedRecipesUseCase = getUserSavedRecipesUseCase,
            getCurrentUserUseCase = getCurrentUserUseCase,
            getSavedRecipeIdUseCase = getSavedRecipeIdUseCase
        )

        composeRule.activity.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.route
            ) {
                composable(
                    route = Screen.HomeScreen.route
                ) {
                    HomeScreen(
                        navController = navController,
                        viewModel = homeViewModel
                    )
                }

                composable(
                    route = Screen.RecipeDetailsScreen.route + "recipeId={recipeId}",
                    arguments = listOf(
                        navArgument(
                            name = "recipeId"
                        ) {
                            type = NavType.StringType
                        }
                    )
                ) {
                    RecipeDetailsScreen(
                        navController = navController,
                        viewModel = recipeDetailsViewModel
                    )
                }
            }
        }
    }

    private fun setMocks() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(getIngredients()))
        coEvery { getRecipesUseCase(any(), any(), any()) } returns flowOf(Resource.Success(getRecipes()))
        coEvery { getUserShoppingListsUseCase(any(), any()) } returns flowOf(Resource.Success(getShoppingLists()))
        coEvery { addSearchSuggestionUseCase(any()) } returns flowOf(Resource.Success(true))
        coEvery { getSearchSuggestionsUseCase() } returns flowOf(Resource.Success(getSearchSuggestions()))
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Success(getCategories()))

        every { savedStateHandle.get<String>(any()) } returns "recipeId"
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        coEvery { getUserSavedRecipesUseCase(any(), any(), any()) } returns flowOf(Resource.Success(getRecipes()))
        every { getCurrentUserUseCase() } returns firebaseUser
        every { firebaseUser.uid } returns "userUID"
    }

    @Test
    fun clickOnRecipe_navigateToRecipeDetails_andBackToHome() {
        setMocks()
        setScreen()

        composeRule.onNodeWithTag("Home Content").isDisplayed()
        composeRule.onNodeWithTag("Recipe recipeId").performScrollTo()
        composeRule.onNodeWithTag("Recipe recipeId").performClick()

        composeRule.onNodeWithTag("Recipe Details Content").assertIsDisplayed()
        composeRule.onNodeWithText("Ingredients").isDisplayed()
        composeRule.onNodeWithText("Description").isDisplayed()
        composeRule.onNodeWithTag("Ingredients Tab Content").isDisplayed()
        composeRule.onNodeWithTag("Description Tab Content").isNotDisplayed()

        composeRule.onNodeWithText(recipeWithIngredients.name).isDisplayed()
        composeRule.onNodeWithText(recipeWithIngredients.ingredients.keys.size.toString()).isDisplayed()
        for(ingredient in recipeWithIngredients.ingredients) {
            composeRule.onNodeWithText(ingredient.value).isDisplayed()
            composeRule.onNodeWithText(ingredient.key.name).isDisplayed()
        }

        composeRule.onNodeWithTag("Description Tab Title").performClick()
        composeRule.onNodeWithTag("Ingredients Tab Content").isNotDisplayed()
        composeRule.onNodeWithTag("Description Tab Content").isDisplayed()

        composeRule.onNodeWithContentDescription("Back button").performClick()
        composeRule.onNodeWithTag("Home Content").isDisplayed()
        composeRule.onNodeWithTag("Recipe Details Content").assertIsNotDisplayed()

        coVerify {
            getCategoriesUseCase()
            getRecipesUseCase(any(), any(), any())
            getIngredientsUseCase()
            getUserShoppingListsUseCase(any(), any())

            savedStateHandle.get<String>("recipeId")
            getRecipeUseCase("recipeId")
            getCurrentUserUseCase()
            firebaseUser.uid
            getUserSavedRecipesUseCase("userUID", "", true)
        }
        confirmVerified()
    }

    @Test
    fun clickOnSearchBar_searchViewIsVisible_textInput() {
        setMocks()
        setOnlyHomeScreen()

        composeRule.onNodeWithTag("Home Lazy Column").assertIsDisplayed()
        composeRule.onNodeWithTag("Search Bar").performClick()
        composeRule.onNodeWithTag("Home Lazy Column").assertIsNotDisplayed()

        composeRule.onAllNodes(hasSetTextAction())[0].performTextInput("Recipe") //finds search view editable text
        composeRule.onNodeWithText("Recipe").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Clear").performClick() //clears text
        composeRule.onNodeWithText("Recipe").assertIsNotDisplayed()
        composeRule.onNodeWithContentDescription("Clear").performClick() //closes search view

        composeRule.onNodeWithTag("Home Lazy Column").assertIsDisplayed()
        composeRule.onNodeWithTag("Search Bar").performClick()
        composeRule.onNodeWithTag("Home Lazy Column").assertIsNotDisplayed()

        composeRule.onNodeWithText("Search Suggestion Text").performClick()
        composeRule.onNodeWithContentDescription("Clear").performClick()
        composeRule.onNodeWithContentDescription("Clear").performClick()

        composeRule.onNodeWithTag("Home Lazy Column").assertIsDisplayed()
        composeRule.onNodeWithTag("Search Bar").performClick()

        composeRule.onAllNodes(hasSetTextAction())[0].performTextInput("Recipe")
        composeRule.onNodeWithText("Recipe").performImeAction()
        composeRule.onNodeWithTag("Home Lazy Column").assertIsDisplayed()
        composeRule.onNodeWithText("Recipe").assertIsDisplayed()

        coVerifySequence {
            getCategoriesUseCase()
            getRecipesUseCase(any(), "", any())
            getIngredientsUseCase()
            getUserShoppingListsUseCase(any(), any())
            getSearchSuggestionsUseCase()
            getRecipesUseCase(any(), "", any())
            getSearchSuggestionsUseCase()
            getRecipesUseCase(any(), "", any())
            getSearchSuggestionsUseCase()
            addSearchSuggestionUseCase(any())
            getRecipesUseCase(any(), "Recipe", any())
        }
        confirmVerified()
    }

    @Test
    fun categoriesSection_categoriesAreClickable_namesAreVisible() {
        setMocks()
        setOnlyHomeScreen()

        composeRule.onNodeWithTag("Home Lazy Column").assertIsDisplayed()
        composeRule.onNodeWithTag("Categories Section").assertIsDisplayed()

        val categories = homeViewModel.homeState.value.categories
        for(category in categories) {
            composeRule
                .onNodeWithTag("Categories Section Lazy Row")
                .performScrollToNode(
                    hasTestTag("${category.categoryId} category")
                )
            composeRule.onNodeWithTag("${category.categoryId} category").assertIsDisplayed()
            composeRule.onNodeWithTag("${category.categoryId} category").assertTextEquals(category.categoryId)
            composeRule.onNodeWithTag("${category.categoryId} category").performClick()
        }

        composeRule.onNodeWithTag("Stew category").performClick()

        for(category in categories.reversed()) {
            composeRule
                .onNodeWithTag("Categories Section Lazy Row")
                .performScrollToNode(
                    hasTestTag("${category.categoryId} category")
                )
            composeRule.onNodeWithTag("${category.categoryId} category").assertIsDisplayed()
            composeRule.onNodeWithTag("${category.categoryId} category").performClick()
        }

        coVerifySequence {
            getCategoriesUseCase()
            getRecipesUseCase(any(), "", any())
            getIngredientsUseCase()
            getUserShoppingListsUseCase(any(), any())
            for(category in categories) {
                getRecipesUseCase(any(), "", category.categoryId)
            }
            getRecipesUseCase(any(), "", any())
            for(category in categories.reversed()) {
                getRecipesUseCase(any(), "", category.categoryId)
            }
        }
        confirmVerified()
    }

    @Test
    fun sortRecipesByDate() {
        setMocks()
        setOnlyHomeScreen()

        composeRule.onNodeWithText("Newest").assertIsDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsNotDisplayed()

        composeRule.onNodeWithTag("Recipe recipe2Id").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsDisplayed()

        composeRule.onNodeWithText("Newest").performClick()

        composeRule.onNodeWithText("Newest").assertIsNotDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsDisplayed()

        composeRule.onNodeWithTag("Recipe recipe2Id").assertIsDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsNotDisplayed()

        composeRule.onNodeWithText("Oldest").performClick()

        composeRule.onNodeWithText("Newest").assertIsDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsNotDisplayed()

        composeRule.onNodeWithTag("Recipe recipe2Id").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsDisplayed()

        coVerifySequence {
            getCategoriesUseCase()
            getRecipesUseCase(any(), "", any())
            getIngredientsUseCase()
            getUserShoppingListsUseCase(any(), any())
        }
        confirmVerified()
    }
}