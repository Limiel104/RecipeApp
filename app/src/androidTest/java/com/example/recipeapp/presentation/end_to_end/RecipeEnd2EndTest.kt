package com.example.recipeapp.presentation.end_to_end

import androidx.activity.compose.setContent
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
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
import com.example.recipeapp.domain.model.User
import com.example.recipeapp.domain.use_case.AddSavedRecipeUseCase
import com.example.recipeapp.domain.use_case.AddSearchSuggestionUseCase
import com.example.recipeapp.domain.use_case.AddUserUseCase
import com.example.recipeapp.domain.use_case.DeleteSavedRecipeUseCase
import com.example.recipeapp.domain.use_case.GetCategoriesUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.domain.use_case.GetRecipeUseCase
import com.example.recipeapp.domain.use_case.GetRecipesUseCase
import com.example.recipeapp.domain.use_case.GetSavedRecipeIdUseCase
import com.example.recipeapp.domain.use_case.GetSearchSuggestionsUseCase
import com.example.recipeapp.domain.use_case.GetUserRecipesUseCase
import com.example.recipeapp.domain.use_case.GetUserSavedRecipesUseCase
import com.example.recipeapp.domain.use_case.GetUserShoppingListsUseCase
import com.example.recipeapp.domain.use_case.GetUserUseCase
import com.example.recipeapp.domain.use_case.LoginUseCase
import com.example.recipeapp.domain.use_case.LogoutUseCase
import com.example.recipeapp.domain.use_case.SignupUseCase
import com.example.recipeapp.domain.use_case.SortRecipesUseCase
import com.example.recipeapp.domain.use_case.UpdateUserPasswordUseCase
import com.example.recipeapp.domain.use_case.UpdateUserUseCase
import com.example.recipeapp.domain.use_case.ValidateConfirmPasswordUseCase
import com.example.recipeapp.domain.use_case.ValidateEmailUseCase
import com.example.recipeapp.domain.use_case.ValidateLoginPasswordUseCase
import com.example.recipeapp.domain.use_case.ValidateNameUseCase
import com.example.recipeapp.domain.use_case.ValidateSignupPasswordUseCase
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.presentation.account.AccountViewModel
import com.example.recipeapp.presentation.account.composable.AccountScreen
import com.example.recipeapp.presentation.common.getCategories
import com.example.recipeapp.presentation.common.getIngredients
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.example.recipeapp.presentation.common.getRecipes
import com.example.recipeapp.presentation.common.getSearchSuggestions
import com.example.recipeapp.presentation.common.getShoppingLists
import com.example.recipeapp.presentation.home.HomeViewModel
import com.example.recipeapp.presentation.home.composable.HomeScreen
import com.example.recipeapp.presentation.login.LoginViewModel
import com.example.recipeapp.presentation.login.composable.LoginScreen
import com.example.recipeapp.presentation.navigation.Screen
import com.example.recipeapp.presentation.recipe_details.RecipeDetailsViewModel
import com.example.recipeapp.presentation.recipe_details.composable.RecipeDetailsScreen
import com.example.recipeapp.presentation.saved_recipes.SavedRecipesViewModel
import com.example.recipeapp.presentation.saved_recipes.composable.SavedRecipesScreen
import com.example.recipeapp.presentation.signup.SignupViewModel
import com.example.recipeapp.presentation.signup.composable.SignupScreen
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
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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

    private lateinit var savedRecipesViewModel: SavedRecipesViewModel

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var loginViewModel: LoginViewModel

    private lateinit var signupUseCase: SignupUseCase
    private lateinit var addUserUseCase: AddUserUseCase
    private lateinit var signupViewModel: SignupViewModel

    private lateinit var updateUserPasswordUseCase: UpdateUserPasswordUseCase
    private lateinit var getUserRecipesUseCase: GetUserRecipesUseCase
    private lateinit var updateUserUseCase: UpdateUserUseCase
    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var accountViewModel: AccountViewModel

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

        loginUseCase = mockk()
        signupUseCase = mockk()
        addUserUseCase = mockk()

        updateUserPasswordUseCase = mockk()
        getUserRecipesUseCase = mockk()
        updateUserUseCase = mockk()
        getUserUseCase = mockk()
        logoutUseCase = mockk()

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

    private fun setViewModels(
        setHomeVM: Boolean = false,
        setRecipeDetailsVM: Boolean = false,
        setSavedRecipesVM: Boolean = false,
        setLoginVM: Boolean = false,
        setSignupVM: Boolean = false,
        setAccountVM: Boolean = false
    ) {
        if(setHomeVM) {
            homeViewModel = HomeViewModel(
                getIngredientsUseCase = getIngredientsUseCase,
                getRecipesUseCase = getRecipesUseCase,
                getUserShoppingListsUseCase = getUserShoppingListsUseCase,
                addSearchSuggestionUseCase = addSearchSuggestionUseCase,
                getSearchSuggestionsUseCase = getSearchSuggestionsUseCase,
                getCategoriesUseCase = getCategoriesUseCase,
                sortRecipesUseCase = SortRecipesUseCase()
            )
        }

        if(setRecipeDetailsVM) {
            recipeDetailsViewModel = RecipeDetailsViewModel(
                savedStateHandle = savedStateHandle,
                getRecipeUseCase = getRecipeUseCase,
                addSavedRecipeUseCase = addSavedRecipeUseCase,
                deleteSavedRecipeUseCase = deleteSavedRecipeUseCase,
                getUserSavedRecipesUseCase = getUserSavedRecipesUseCase,
                getCurrentUserUseCase = getCurrentUserUseCase,
                getSavedRecipeIdUseCase = getSavedRecipeIdUseCase
            )
        }

        if(setSavedRecipesVM) {
            savedRecipesViewModel = SavedRecipesViewModel(
                getCurrentUserUseCase = getCurrentUserUseCase,
                getSavedRecipeIdUseCase = getSavedRecipeIdUseCase,
                deleteSavedRecipeUseCase = deleteSavedRecipeUseCase,
                getUserSavedRecipesUseCase = getUserSavedRecipesUseCase,
                addSearchSuggestionUseCase = addSearchSuggestionUseCase,
                getSearchSuggestionsUseCase = getSearchSuggestionsUseCase,
                sortRecipesUseCase = SortRecipesUseCase()
            )
        }

        if(setLoginVM) {
            loginViewModel = LoginViewModel(
                savedStateHandle = savedStateHandle,
                loginUseCase = loginUseCase,
                validateEmailUseCase = ValidateEmailUseCase(),
                validateLoginPasswordUseCase = ValidateLoginPasswordUseCase()
            )
        }

        if(setSignupVM) {
            signupViewModel = SignupViewModel(
                savedStateHandle = savedStateHandle,
                signupUseCase = signupUseCase,
                validateEmailUseCase = ValidateEmailUseCase(),
                validateSignupPasswordUseCase = ValidateSignupPasswordUseCase(),
                validateConfirmPasswordUseCase = ValidateConfirmPasswordUseCase(),
                validateNameUseCase = ValidateNameUseCase(),
                getCurrentUserUseCase = getCurrentUserUseCase,
                addUserUseCase = addUserUseCase
            )
        }

        if(setAccountVM) {
            accountViewModel = AccountViewModel(
                updateUserPasswordUseCase = updateUserPasswordUseCase,
                getCurrentUserUseCase = getCurrentUserUseCase,
                getUserRecipesUseCase = getUserRecipesUseCase,
                sortRecipesUseCase = SortRecipesUseCase(),
                updateUserUseCase = updateUserUseCase,
                validateSignupPasswordUseCase = ValidateSignupPasswordUseCase(),
                validateConfirmPasswordUseCase = ValidateConfirmPasswordUseCase(),
                validateNameUseCase = ValidateNameUseCase(),
                getUserUseCase = getUserUseCase,
                logoutUseCase = logoutUseCase
            )
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun setOnlyHomeScreen() {
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

    private fun setHomeAndRecipeDetailsScreens() {
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

    private fun setSavedRecipesLoginAndSignupScreens() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.SavedRecipesScreen.route
            ) {
                composable(
                    route = Screen.SavedRecipesScreen.route
                ) {
                    SavedRecipesScreen(
                        navController = navController,
                        viewModel = savedRecipesViewModel
                    )
                }

                composable(
                    route = Screen.LoginScreen.route + "lastDestination={lastDestination}",
                    arguments = listOf(
                        navArgument(
                            name = "lastDestination"
                        ) {
                            type = NavType.StringType
                        }
                    )
                ) {
                    LoginScreen(
                        navController = navController,
                        viewModel = loginViewModel
                    )
                }

                composable(
                    route = Screen.SignupScreen.route + "lastDestination={lastDestination}",
                    arguments = listOf(
                        navArgument(
                            name = "lastDestination"
                        ) {
                            type = NavType.StringType
                        }
                    )
                ) {
                    SignupScreen(
                        navController = navController,
                        viewModel = signupViewModel
                    )
                }
            }
        }
    }

    private fun setSavedRecipesAndRecipeDetailsScreens() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.SavedRecipesScreen.route
            ) {
                composable(
                    route = Screen.SavedRecipesScreen.route
                ) {
                    SavedRecipesScreen(
                        navController = navController,
                        viewModel = savedRecipesViewModel
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

    private fun setOnlySavedRecipesScreen() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.SavedRecipesScreen.route
            ) {
                composable(
                    route = Screen.SavedRecipesScreen.route
                ) {
                    SavedRecipesScreen(
                        navController = navController,
                        viewModel = savedRecipesViewModel
                    )
                }
            }
        }
    }

    private fun setAccountLoginAndSignupScreens() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.AccountScreen.route
            ) {
                composable(
                    route = Screen.AccountScreen.route
                ) {
                    AccountScreen(
                        navController = navController,
                        viewModel = accountViewModel
                    )
                }

                composable(
                    route = Screen.LoginScreen.route + "lastDestination={lastDestination}",
                    arguments = listOf(
                        navArgument(
                            name = "lastDestination"
                        ) {
                            type = NavType.StringType
                        }
                    )
                ) {
                    LoginScreen(
                        navController = navController,
                        viewModel = loginViewModel
                    )
                }

                composable(
                    route = Screen.SignupScreen.route + "lastDestination={lastDestination}",
                    arguments = listOf(
                        navArgument(
                            name = "lastDestination"
                        ) {
                            type = NavType.StringType
                        }
                    )
                ) {
                    SignupScreen(
                        navController = navController,
                        viewModel = signupViewModel
                    )
                }
            }
        }
    }

    private fun setAccountAndRecipeDetailsScreens() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.AccountScreen.route
            ) {
                composable(
                    route = Screen.AccountScreen.route
                ) {
                    AccountScreen(
                        navController = navController,
                        viewModel = accountViewModel
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

    private fun setOnlyAccountScreen() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.AccountScreen.route
            ) {
                composable(
                    route = Screen.AccountScreen.route
                ) {
                    AccountScreen(
                        navController = navController,
                        viewModel = accountViewModel
                    )
                }
            }
        }
    }

    private fun setMocks(
        userRecipesMultipleReturns: Boolean = false,
        lastDestinationValue: String = ""
    ) {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(getIngredients()))
        coEvery { getRecipesUseCase(any(), any(), any()) } returns flowOf(Resource.Success(getRecipes()))
        coEvery { getUserShoppingListsUseCase(any(), any()) } returns flowOf(Resource.Success(getShoppingLists()))
        coEvery { addSearchSuggestionUseCase(any()) } returns flowOf(Resource.Success(true))
        coEvery { getSearchSuggestionsUseCase() } returns flowOf(Resource.Success(getSearchSuggestions()))
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Success(getCategories()))

        every { savedStateHandle.get<String>("recipeId") } returns "recipeId"
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))

        if(userRecipesMultipleReturns) {
            coEvery { getUserSavedRecipesUseCase(any(), any(), any())
            } returns flowOf(Resource.Success(getRecipes())) andThen flowOf(Resource.Success(getRecipes().filter { recipe ->
                recipe.recipeId != "recipeId"
            }))
        }
        else {
            coEvery { getUserSavedRecipesUseCase(any(), any(), any())
            } returns flowOf(Resource.Success(getRecipes()))
        }

        every { firebaseUser.uid } returns "userUID"

        every { savedStateHandle.get<String>("lastDestination") } returns lastDestinationValue
        coEvery { loginUseCase(any(), any()) } returns flowOf(Resource.Success(firebaseUser))
        coEvery { signupUseCase(any(), any()) } returns flowOf(Resource.Success(firebaseUser))
        coEvery { addUserUseCase(any()) } returns flowOf(Resource.Success(true))
        coEvery { getSavedRecipeIdUseCase(any(), any()) } returns flowOf(Resource.Success("savedRecipeId"))
        coEvery { deleteSavedRecipeUseCase(any()) } returns flowOf(Resource.Success(true))

        coEvery { updateUserPasswordUseCase(any()) } returns flowOf(Resource.Success(true))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(getRecipes()))
        coEvery { updateUserUseCase(any()) } returns flowOf(Resource.Success(true))
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(User("userUID", "UserName")))
        every { logoutUseCase() } just runs
    }

    private fun setUserIsNotLoggedInInitiallyMock() {
        every { getCurrentUserUseCase() } returns null andThen firebaseUser
    }

    private fun setUserIsLoggedInMock() {
        every { getCurrentUserUseCase() } returns firebaseUser
    }

    @Test
    fun clickOnRecipe_navigateToRecipeDetails_andBackToHome() {
        setMocks()
        setUserIsLoggedInMock()
        setViewModels(
            setHomeVM = true,
            setRecipeDetailsVM = true
        )
        setHomeAndRecipeDetailsScreens()

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
        setUserIsLoggedInMock()
        setViewModels(setHomeVM = true)
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
        setUserIsLoggedInMock()
        setViewModels(setHomeVM = true)
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
        setUserIsLoggedInMock()
        setViewModels(setHomeVM = true)
        setOnlyHomeScreen()

        composeRule.onNodeWithText("Newest").assertIsDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsNotDisplayed()

        composeRule.onNodeWithTag("Recipe recipe7Id").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsDisplayed()

        composeRule.onNodeWithText("Newest").performClick()

        composeRule.onNodeWithText("Newest").assertIsNotDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsDisplayed()

        composeRule.onNodeWithTag("Recipe recipe7Id").assertIsDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsNotDisplayed()

        composeRule.onNodeWithText("Oldest").performClick()

        composeRule.onNodeWithText("Newest").assertIsDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsNotDisplayed()

        composeRule.onNodeWithTag("Recipe recipe7Id").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsDisplayed()

        coVerifySequence {
            getCategoriesUseCase()
            getRecipesUseCase(any(), "", any())
            getIngredientsUseCase()
            getUserShoppingListsUseCase(any(), any())
        }
        confirmVerified()
    }

    @Test
    fun savedRecipesShowingUserNotLoggedInScreen_navigateToLoginScreen_thenNavigateBackToSavedRecipes() {
        setMocks(lastDestinationValue = Screen.SavedRecipesScreen.route)
        setUserIsNotLoggedInInitiallyMock()
        setViewModels(
            setSavedRecipesVM = true,
            setLoginVM = true
        )
        setSavedRecipesLoginAndSignupScreens()

        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()
        composeRule.onNodeWithText("You are not logged in").assertIsDisplayed()
        composeRule.onNodeWithText("Login or Signup").assertIsDisplayed()

        composeRule.onNodeWithTag("Login button").assertIsDisplayed()
        composeRule.onNodeWithTag("Login button").performClick()
        composeRule.onNodeWithTag("Login Content").assertIsDisplayed()

        composeRule.onNodeWithTag("Login email TF").performTextInput("email@test.com")
        composeRule.onNodeWithText("email@test.com").assertIsDisplayed()

        composeRule.onNodeWithTag("Login password TF").performTextInput("Password1+")
        val passwordNode = composeRule.onNodeWithTag("Login password TF").fetchSemanticsNode()
        val textInput = passwordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        Truth.assertThat(textInput).isEqualTo("••••••••••")

        composeRule.onNodeWithTag("Login button").performClick()
        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()

        coVerifySequence {
            getCurrentUserUseCase()
            savedStateHandle.get<String>("lastDestination")
            loginUseCase("email@test.com", "Password1+")
        }
        confirmVerified()
    }

    @Test
    fun savedRecipesShowingUserNotLoggedInScreen_navigateToSignupScreen_thenNavigateBackToSavedRecipes() {
        setMocks(lastDestinationValue = Screen.SavedRecipesScreen.route)
        setUserIsNotLoggedInInitiallyMock()
        setViewModels(
            setSavedRecipesVM = true,
            setSignupVM = true
        )
        setSavedRecipesLoginAndSignupScreens()

        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()
        composeRule.onNodeWithText("You are not logged in").assertIsDisplayed()
        composeRule.onNodeWithText("Login or Signup").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup button").assertIsDisplayed()
        composeRule.onNodeWithTag("Signup button").performClick()
        composeRule.onNodeWithTag("Signup Content").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup email TF").performTextInput("email@test.com")
        composeRule.onNodeWithText("email@test.com").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup password TF").performTextInput("Password1+")
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val textInput = passwordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        Truth.assertThat(textInput).isEqualTo("Password1+")

        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput("Password1+")
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val textInput2 = confirmPasswordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        Truth.assertThat(textInput2).isEqualTo("Password1+")

        composeRule.onNodeWithTag("Signup name TF").performTextInput("John Smith")
        composeRule.onNodeWithText("John Smith").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup button").performClick()
        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()


        coVerifySequence {
            getCurrentUserUseCase()
            savedStateHandle.get<String>("lastDestination")
            signupUseCase("email@test.com", "Password1+")
            getCurrentUserUseCase()
            firebaseUser.uid
            addUserUseCase(any())
        }
        confirmVerified()
    }

    @Test
    fun savedRecipesShowingUserNotLoggedInScreen_navigateToSignupFromLoginScreen_thenNavigateBackToSavedRecipes() {
        setMocks(lastDestinationValue = Screen.SavedRecipesScreen.route)
        setUserIsNotLoggedInInitiallyMock()
        setViewModels(
            setSavedRecipesVM = true,
            setLoginVM = true,
            setSignupVM = true
        )
        setSavedRecipesLoginAndSignupScreens()

        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()
        composeRule.onNodeWithText("You are not logged in").assertIsDisplayed()
        composeRule.onNodeWithText("Login or Signup").assertIsDisplayed()

        composeRule.onNodeWithTag("Login button").assertIsDisplayed()
        composeRule.onNodeWithTag("Login button").performClick()
        composeRule.onNodeWithTag("Login Content").assertIsDisplayed()

        composeRule.onNodeWithText("Don't have an account?").assertIsDisplayed()
        composeRule.onNodeWithText("Signup").performClick()
        composeRule.onNodeWithTag("Signup Content").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup email TF").performTextInput("email@test.com")
        composeRule.onNodeWithText("email@test.com").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup password TF").performTextInput("Password1+")
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val textInput = passwordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        Truth.assertThat(textInput).isEqualTo("Password1+")

        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput("Password1+")
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val textInput2 = confirmPasswordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        Truth.assertThat(textInput2).isEqualTo("Password1+")

        composeRule.onNodeWithTag("Signup name TF").performTextInput("John Smith")
        composeRule.onNodeWithText("John Smith").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup button").performClick()
        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()

        coVerifySequence {
            getCurrentUserUseCase()
            savedStateHandle.get<String>("lastDestination")
            savedStateHandle.get<String>("lastDestination")
            signupUseCase("email@test.com", "Password1+")
            getCurrentUserUseCase()
            firebaseUser.uid
            addUserUseCase(any())
        }
        confirmVerified()
    }

    @Test
    fun clickOnSavedRecipe_navigateToRecipeDetails_andBackToSavedRecipes() {
        setMocks()
        setUserIsLoggedInMock()
        setViewModels(
            setSavedRecipesVM = true,
            setRecipeDetailsVM = true
        )
        setSavedRecipesAndRecipeDetailsScreens()

        composeRule.onNodeWithTag("Saved Recipes Content").assertIsDisplayed()
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
        composeRule.onNodeWithTag("Saved Recipes Content").isDisplayed()
        composeRule.onNodeWithTag("Recipe Details Content").assertIsNotDisplayed()

        coVerifySequence {
            savedStateHandle.get<String>("recipeId")
            getRecipeUseCase(any())
            getCurrentUserUseCase()
            firebaseUser.uid
            getUserSavedRecipesUseCase(any(), "", any())
            getCurrentUserUseCase()
            firebaseUser.uid
            firebaseUser.uid
            getUserSavedRecipesUseCase(any(), "", any())
        }
        confirmVerified()
    }

    @Test
    fun clickOnSavedRecipesSearchBar_searchViewIsVisible_textInput() {
        setMocks()
        setUserIsLoggedInMock()
        setViewModels(setSavedRecipesVM = true)
        setOnlySavedRecipesScreen()

        composeRule.onNodeWithTag("Saved Recipes Lazy Column").assertIsDisplayed()
        composeRule.onNodeWithTag("Search Bar").performClick()
        composeRule.onNodeWithTag("Saved Recipes Lazy Column").assertIsNotDisplayed()

        composeRule.onAllNodes(hasSetTextAction())[0].performTextInput("Recipe") //finds search view editable text
        composeRule.onNodeWithText("Recipe").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Clear").performClick() //clears text
        composeRule.onNodeWithText("Recipe").assertIsNotDisplayed()
        composeRule.onNodeWithContentDescription("Clear").performClick() //closes search view

        composeRule.onNodeWithTag("Saved Recipes Lazy Column").assertIsDisplayed()
        composeRule.onNodeWithTag("Search Bar").performClick()
        composeRule.onNodeWithTag("Saved Recipes Lazy Column").assertIsNotDisplayed()

        composeRule.onNodeWithText("Search Suggestion Text").performClick()
        composeRule.onNodeWithContentDescription("Clear").performClick()
        composeRule.onNodeWithContentDescription("Clear").performClick()

        composeRule.onNodeWithTag("Saved Recipes Lazy Column").assertIsDisplayed()
        composeRule.onNodeWithTag("Search Bar").performClick()

        composeRule.onAllNodes(hasSetTextAction())[0].performTextInput("Recipe")
        composeRule.onNodeWithText("Recipe").performImeAction()
        composeRule.onNodeWithTag("Saved Recipes Lazy Column").assertIsDisplayed()
        composeRule.onNodeWithText("Recipe").assertIsDisplayed()

        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
            firebaseUser.uid
            getUserSavedRecipesUseCase(any(), "", any())
            getSearchSuggestionsUseCase()
            getUserSavedRecipesUseCase(any(), "", any())
            getSearchSuggestionsUseCase()
            getUserSavedRecipesUseCase(any(), "", any())
            getSearchSuggestionsUseCase()
            addSearchSuggestionUseCase(any())
            getUserSavedRecipesUseCase(any(), "Recipe", any())
        }
        confirmVerified()
    }

    @Test
    fun sortSavedRecipesByDate() {
        setMocks()
        setUserIsLoggedInMock()
        setViewModels(setSavedRecipesVM = true)
        setOnlySavedRecipesScreen()

        composeRule.onNodeWithText("Newest").assertIsDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsNotDisplayed()

        composeRule.onNodeWithTag("Recipe recipe7Id").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsDisplayed()

        composeRule.onNodeWithText("Newest").performClick()

        composeRule.onNodeWithText("Newest").assertIsNotDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsDisplayed()

        composeRule.onNodeWithTag("Recipe recipe7Id").assertIsDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsNotDisplayed()

        composeRule.onNodeWithText("Oldest").performClick()

        composeRule.onNodeWithText("Newest").assertIsDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsNotDisplayed()

        composeRule.onNodeWithTag("Recipe recipe7Id").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsDisplayed()

        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
            firebaseUser.uid
            getUserSavedRecipesUseCase(any(), "", any())
        }
        confirmVerified()
    }

    @Test
    fun clickOnSavedRecipeBookmarkIcon_removeItFromUserSavedRecipes() {
        setMocks(userRecipesMultipleReturns = true)
        setUserIsLoggedInMock()
        setViewModels(setSavedRecipesVM = true)
        setOnlySavedRecipesScreen()

        composeRule.onNodeWithTag("Recipe recipeId")
            .onChildren()
            .filterToOne(hasContentDescription("Icon button"))
            .performClick() //find bookmark icon and perform click

        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
            firebaseUser.uid
            getUserSavedRecipesUseCase(any(), "", any())
            getSavedRecipeIdUseCase(any(), any())
            deleteSavedRecipeUseCase(any())
            getUserSavedRecipesUseCase(any(), "", any())
        }
        confirmVerified()
        Truth.assertThat(savedRecipesViewModel.savedRecipesState.value.savedRecipes.size).isEqualTo(6)
    }

    @Test
    fun accountShowingUserNotLoggedInScreen_navigateToLoginScreen_thenNavigateBackToAccount() {
        setMocks(lastDestinationValue = Screen.AccountScreen.route)
        setUserIsNotLoggedInInitiallyMock()
        setViewModels(
            setLoginVM = true,
            setAccountVM = true
        )
        setAccountLoginAndSignupScreens()

        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()
        composeRule.onNodeWithText("You are not logged in").assertIsDisplayed()
        composeRule.onNodeWithText("Login or Signup").assertIsDisplayed()

        composeRule.onNodeWithTag("Login button").assertIsDisplayed()
        composeRule.onNodeWithTag("Login button").performClick()
        composeRule.onNodeWithTag("Login Content").assertIsDisplayed()

        composeRule.onNodeWithTag("Login email TF").performTextInput("email@test.com")
        composeRule.onNodeWithText("email@test.com").assertIsDisplayed()

        composeRule.onNodeWithTag("Login password TF").performTextInput("Password1+")
        val passwordNode = composeRule.onNodeWithTag("Login password TF").fetchSemanticsNode()
        val textInput = passwordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        Truth.assertThat(textInput).isEqualTo("••••••••••")

        composeRule.onNodeWithTag("Login button").performClick()
        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()

        coVerifySequence {
            savedStateHandle.get<String>("lastDestination")
            getCurrentUserUseCase()
            loginUseCase("email@test.com", "Password1+")
        }
        confirmVerified()
    }

    @Test
    fun accountShowingUserNotLoggedInScreen_navigateToSignupScreen_thenNavigateBackToAccount() {
        setMocks(lastDestinationValue = Screen.AccountScreen.route)
        setUserIsNotLoggedInInitiallyMock()
        setViewModels(
            setSignupVM = true,
            setAccountVM = true
        )
        setAccountLoginAndSignupScreens()

        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()
        composeRule.onNodeWithText("You are not logged in").assertIsDisplayed()
        composeRule.onNodeWithText("Login or Signup").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup button").assertIsDisplayed()
        composeRule.onNodeWithTag("Signup button").performClick()
        composeRule.onNodeWithTag("Signup Content").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup email TF").performTextInput("email@test.com")
        composeRule.onNodeWithText("email@test.com").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup password TF").performTextInput("Password1+")
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val textInput = passwordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        Truth.assertThat(textInput).isEqualTo("Password1+")

        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput("Password1+")
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val textInput2 = confirmPasswordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        Truth.assertThat(textInput2).isEqualTo("Password1+")

        composeRule.onNodeWithTag("Signup name TF").performTextInput("John Smith")
        composeRule.onNodeWithText("John Smith").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup button").performClick()
        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()


        coVerifySequence {
            savedStateHandle.get<String>("lastDestination")
            getCurrentUserUseCase()
            signupUseCase("email@test.com", "Password1+")
            getCurrentUserUseCase()
            firebaseUser.uid
            addUserUseCase(any())
        }
        confirmVerified()
    }

    @Test
    fun accountShowingUserNotLoggedInScreen_navigateToSignupFromLoginScreen_thenNavigateBackToAccount() {
        setMocks(lastDestinationValue = Screen.AccountScreen.route)
        setUserIsNotLoggedInInitiallyMock()
        setViewModels(
            setLoginVM = true,
            setSignupVM = true,
            setAccountVM = true
        )
        setAccountLoginAndSignupScreens()

        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()
        composeRule.onNodeWithText("You are not logged in").assertIsDisplayed()
        composeRule.onNodeWithText("Login or Signup").assertIsDisplayed()

        composeRule.onNodeWithTag("Login button").assertIsDisplayed()
        composeRule.onNodeWithTag("Login button").performClick()
        composeRule.onNodeWithTag("Login Content").assertIsDisplayed()

        composeRule.onNodeWithText("Don't have an account?").assertIsDisplayed()
        composeRule.onNodeWithText("Signup").performClick()
        composeRule.onNodeWithTag("Signup Content").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup email TF").performTextInput("email@test.com")
        composeRule.onNodeWithText("email@test.com").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup password TF").performTextInput("Password1+")
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val textInput = passwordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        Truth.assertThat(textInput).isEqualTo("Password1+")

        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput("Password1+")
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val textInput2 = confirmPasswordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        Truth.assertThat(textInput2).isEqualTo("Password1+")

        composeRule.onNodeWithTag("Signup name TF").performTextInput("John Smith")
        composeRule.onNodeWithText("John Smith").assertIsDisplayed()

        composeRule.onNodeWithTag("Signup button").performClick()
        composeRule.onNodeWithTag("User not logged in Content").assertIsDisplayed()

        coVerifySequence {
            savedStateHandle.get<String>("lastDestination")
            savedStateHandle.get<String>("lastDestination")
            getCurrentUserUseCase()
            signupUseCase("email@test.com", "Password1+")
            getCurrentUserUseCase()
            firebaseUser.uid
            addUserUseCase(any())
        }
        confirmVerified()
    }

    @Test
    fun clickOnUserRecipe_navigateToRecipeDetails_andBackToAccount() {
        setMocks()
        setUserIsLoggedInMock()
        setViewModels(
            setRecipeDetailsVM = true,
            setAccountVM = true
        )
        setAccountAndRecipeDetailsScreens()

        composeRule.onNodeWithTag("Account Content").assertIsDisplayed()
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
        composeRule.onNodeWithTag("Account Content").isDisplayed()
        composeRule.onNodeWithTag("Recipe Details Content").assertIsNotDisplayed()

        coVerifySequence {
            savedStateHandle.get<String>("recipeId")
            getRecipeUseCase(any())
            getCurrentUserUseCase()
            firebaseUser.uid
            getUserSavedRecipesUseCase(any(), any(), any())
            getCurrentUserUseCase()
            firebaseUser.uid
            getUserUseCase(any())
            firebaseUser.uid
            getUserRecipesUseCase(any())
        }
        confirmVerified()
    }

    @Test
    fun sortUserRecipesByDate() {
        setMocks()
        setUserIsLoggedInMock()
        setViewModels(setAccountVM = true)
        setOnlyAccountScreen()

        composeRule.onNodeWithText("Newest").assertIsDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsNotDisplayed()

        composeRule.onNodeWithTag("Recipe recipe7Id").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsDisplayed()

        composeRule.onNodeWithText("Newest").performClick()

        composeRule.onNodeWithText("Newest").assertIsNotDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsDisplayed()

        composeRule.onNodeWithTag("Recipe recipe7Id").assertIsDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsNotDisplayed()

        composeRule.onNodeWithText("Oldest").performClick()

        composeRule.onNodeWithText("Newest").assertIsDisplayed()
        composeRule.onNodeWithText("Oldest").assertIsNotDisplayed()

        composeRule.onNodeWithTag("Recipe recipe7Id").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Recipe recipe6Id").assertIsDisplayed()

        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
            getUserUseCase(any())
            firebaseUser.uid
            getUserRecipesUseCase(any())
        }
        confirmVerified()
    }

    @Test
    fun editUserDialog_nameTextInput_SaveButtonClosesDialog() {
        setMocks()
        setUserIsLoggedInMock()
        setViewModels(setAccountVM = true)
        setOnlyAccountScreen()

        composeRule.onNodeWithText("UserName").assertIsDisplayed()
        composeRule.onNodeWithTag("Edit dialog").assertIsNotDisplayed()

        composeRule.onNodeWithContentDescription("Edit button").performClick()
        composeRule.onNodeWithTag("Edit dialog").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Clear button").performClick()
        composeRule.onNodeWithTag("Edit dialog").assertIsNotDisplayed()

        composeRule.onNodeWithContentDescription("Edit button").performClick()
        composeRule.onNodeWithTag("Account name TF").performTextInput("New User Name")

        composeRule.onNodeWithTag("Save button").performClick()
        composeRule.onNodeWithTag("Edit dialog").assertIsNotDisplayed()

        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
            getUserUseCase(any())
            firebaseUser.uid
            getUserRecipesUseCase(any())
            updateUserUseCase(any())
        }
        confirmVerified()
    }

    @Test
    fun editUserDialog_noTextInput_saveButtonIsNotClosingDialog() {
        setMocks()
        setUserIsLoggedInMock()
        setViewModels(setAccountVM = true)
        setOnlyAccountScreen()

        composeRule.onNodeWithTag("Edit dialog").assertIsNotDisplayed()

        composeRule.onNodeWithContentDescription("Edit button").performClick()
        composeRule.onNodeWithTag("Edit dialog").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Clear button").performClick()
        composeRule.onNodeWithTag("Edit dialog").assertIsNotDisplayed()

        composeRule.onNodeWithContentDescription("Edit button").performClick()
        composeRule.onNodeWithTag("Save button").performClick()
        composeRule.onNodeWithTag("Edit dialog").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Clear button").performClick()
        composeRule.onNodeWithTag("Edit dialog").assertIsNotDisplayed()

        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
            getUserUseCase(any())
            firebaseUser.uid
            getUserRecipesUseCase(any())
        }
        confirmVerified()
    }
}