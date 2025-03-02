package com.example.recipeapp.presentation.home

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.AddSearchSuggestionUseCase
import com.example.recipeapp.domain.use_case.GetCategoriesUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.domain.use_case.GetRecipesUseCase
import com.example.recipeapp.domain.use_case.GetSearchSuggestionsUseCase
import com.example.recipeapp.domain.use_case.GetUserShoppingListsUseCase
import com.example.recipeapp.domain.use_case.SortRecipesUseCase
import com.example.recipeapp.domain.util.RecipeOrder
import com.example.recipeapp.presentation.common.getCategories
import com.example.recipeapp.presentation.common.getIngredients
import com.example.recipeapp.presentation.common.getRecipes
import com.example.recipeapp.presentation.common.getSearchSuggestions
import com.example.recipeapp.presentation.common.getShoppingLists
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getIngredientsUseCase: GetIngredientsUseCase
    private lateinit var getRecipesUseCase: GetRecipesUseCase
    private lateinit var getUserShoppingListsUseCase: GetUserShoppingListsUseCase
    private lateinit var addSearchSuggestionUseCase: AddSearchSuggestionUseCase
    private lateinit var getSearchSuggestionsUseCase: GetSearchSuggestionsUseCase
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var sortRecipesUseCase: SortRecipesUseCase
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        getRecipesUseCase = mockk(relaxed = true)
        getIngredientsUseCase = mockk(relaxed = true)
        getUserShoppingListsUseCase = mockk(relaxed = true)
        addSearchSuggestionUseCase = mockk(relaxed = true)
        getSearchSuggestionsUseCase= mockk(relaxed = true)
        getCategoriesUseCase = mockk(relaxed = true)
        sortRecipesUseCase = SortRecipesUseCase()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun setViewModel(): HomeViewModel {
        return HomeViewModel(
            getIngredientsUseCase,
            getRecipesUseCase,
            getUserShoppingListsUseCase,
            addSearchSuggestionUseCase,
            getSearchSuggestionsUseCase,
            getCategoriesUseCase,
            sortRecipesUseCase
        )
    }

    private fun getCurrentHomeState(): HomeState {
        return homeViewModel.homeState.value
    }

    @Test
    fun `getRecipes sets recipes successfully`() {
        coEvery { getRecipesUseCase(any(),any(),any()) } returns flowOf(Resource.Success(getRecipes()))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().recipes
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getRecipesUseCase(true,"","") }
        Truth.assertThat(result).containsExactlyElementsIn(getRecipes())
        Truth.assertThat(isLoading).isFalse()
        confirmVerified(getRecipesUseCase)
    }

    @Test
    fun `getRecipes - recipes are sorted correctly`() {
        coEvery { getRecipesUseCase(any(),any(),any()) } returns flowOf(Resource.Success(getRecipes()))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().recipes

        coVerify(exactly = 1) { getRecipesUseCase(true,"","") }
        Truth.assertThat(result).isEqualTo(getRecipes().sortedByDescending { it.date })
    }

    @Test
    fun `getRecipes returns error`() {
        coEvery { getRecipesUseCase(any(),any(),any()) } returns flowOf(Resource.Error("Error message"))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().recipes
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getRecipesUseCase(true,"","") }
        Truth.assertThat(result).isEmpty()
        Truth.assertThat(isLoading).isFalse()
        confirmVerified(getRecipesUseCase)
    }

    @Test
    fun `getRecipes is loading`() {
        coEvery { getRecipesUseCase(any(),any(),any()) } returns flowOf(Resource.Loading(true))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().recipes
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getRecipesUseCase(true,"","") }
        Truth.assertThat(result).isEmpty()
        Truth.assertThat(isLoading).isTrue()
        confirmVerified(getRecipesUseCase)
    }

    @Test
    fun `getIngredients runs successfully`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(getIngredients()))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getIngredientsUseCase() }
        Truth.assertThat(isLoading).isFalse()
        confirmVerified(getIngredientsUseCase)
    }

    @Test
    fun `getIngredients returns error`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Error("Error message"))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getIngredientsUseCase() }
        Truth.assertThat(isLoading).isFalse()
        confirmVerified(getIngredientsUseCase)
    }

    @Test
    fun `getIngredients is loading`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Loading(true))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getIngredientsUseCase() }
        Truth.assertThat(isLoading).isTrue()
        confirmVerified(getIngredientsUseCase)
    }

    @Test
    fun `getShoppingLists runs successfully`() {
        coEvery {
            getUserShoppingListsUseCase("userId",any())
        } returns flowOf(Resource.Success(getShoppingLists()))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getUserShoppingListsUseCase("userId",true) }
        Truth.assertThat(isLoading).isFalse()
        confirmVerified(getUserShoppingListsUseCase)
    }

    @Test
    fun `getShoppingLists returns error`() {
        coEvery { getUserShoppingListsUseCase("userId",any()) } returns flowOf(Resource.Error("Error message"))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getUserShoppingListsUseCase("userId",true) }
        Truth.assertThat(isLoading).isFalse()
        confirmVerified(getUserShoppingListsUseCase)
    }

    @Test
    fun `getShoppingLists is loading`() {
        coEvery { getUserShoppingListsUseCase("userId",any()) } returns flowOf(Resource.Loading(true))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getUserShoppingListsUseCase("userId",true) }
        Truth.assertThat(isLoading).isTrue()
        confirmVerified(getUserShoppingListsUseCase)
    }

    @Test
    fun `getCategories runs successfully`() {
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Success(getCategories()))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().categories
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getCategoriesUseCase() }
        Truth.assertThat(result).isEqualTo(getCategories())
        Truth.assertThat(isLoading).isFalse()
        confirmVerified(getCategoriesUseCase)
    }

    @Test
    fun `getCategories returns error`() {
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Error("Error message"))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().categories
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getCategoriesUseCase() }
        Truth.assertThat(result).isEmpty()
        Truth.assertThat(isLoading).isFalse()
        confirmVerified(getCategoriesUseCase)
    }

    @Test
    fun `getCategories is loading`() {
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Loading(true))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().categories
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getCategoriesUseCase() }
        Truth.assertThat(result).isEmpty()
        Truth.assertThat(isLoading).isTrue()
        confirmVerified(getCategoriesUseCase)
    }

    @Test
    fun `onQueryChanged - initial query empty`() {
        homeViewModel = setViewModel()
        val initialQueryState = getCurrentHomeState().query

        homeViewModel.onEvent(HomeEvent.OnQueryChange("New Query"))
        val resultQueryState = getCurrentHomeState().query

        Truth.assertThat(initialQueryState).isEmpty()
        Truth.assertThat(resultQueryState).isEqualTo("New Query")
    }

    @Test
    fun `onQueryChanged - initial query not empty`() {
        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnQueryChange("Old Query"))
        val initialQueryState = getCurrentHomeState().query

        homeViewModel.onEvent(HomeEvent.OnQueryChange("New Query"))
        val resultQueryState = getCurrentHomeState().query

        Truth.assertThat(initialQueryState).isEqualTo("Old Query")
        Truth.assertThat(resultQueryState).isEqualTo("New Query")
    }

    @Test
    fun `onActiveChange - initially false`() {
        coEvery { getSearchSuggestionsUseCase() } returns flowOf(Resource.Success(getSearchSuggestions()))

        homeViewModel = setViewModel()
        val initialActiveState = getCurrentHomeState().isSearchActive
        val initialSearchSuggestions = getCurrentHomeState().searchSuggestions

        homeViewModel.onEvent(HomeEvent.OnActiveChange)
        val resultActiveState = getCurrentHomeState().isSearchActive
        val resultSearchSuggestions = getCurrentHomeState().searchSuggestions

        coVerify { getSearchSuggestionsUseCase() }
        Truth.assertThat(initialActiveState).isFalse()
        Truth.assertThat(initialSearchSuggestions).isEmpty()
        Truth.assertThat(resultActiveState).isTrue()
        Truth.assertThat(resultSearchSuggestions).isEqualTo(getSearchSuggestions())
        confirmVerified(getSearchSuggestionsUseCase)
    }

    @Test
    fun `onActiveChange - initially true`() {
        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnActiveChange)
        val initialActiveState = getCurrentHomeState().isSearchActive

        homeViewModel.onEvent(HomeEvent.OnActiveChange)
        val resultActiveState = getCurrentHomeState().isSearchActive

        Truth.assertThat(initialActiveState).isTrue()
        Truth.assertThat(resultActiveState).isFalse()
    }

    @Test
    fun `onSearchClicked - isActive false`() {
        coEvery { addSearchSuggestionUseCase(any()) } returns flowOf(Resource.Success(true))
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(getRecipes()))

        homeViewModel = setViewModel()
        val initialActiveState = getCurrentHomeState().isSearchActive

        homeViewModel.onEvent(HomeEvent.OnSearchClicked)
        val resultActiveState = getCurrentHomeState().isSearchActive

        coVerifyOrder {
            getRecipesUseCase(true,"","") //init
            addSearchSuggestionUseCase(any())
            getRecipesUseCase(false,"","")
        }
        Truth.assertThat(initialActiveState).isFalse()
        Truth.assertThat(resultActiveState).isFalse()
        confirmVerified(
            addSearchSuggestionUseCase,
            getRecipesUseCase
        )
    }

    @Test
    fun `onSearchClicked - isActive true`() {
        coEvery { addSearchSuggestionUseCase(any()) } returns flowOf(Resource.Success(true))
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(getRecipes()))

        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnActiveChange)
        val initialActiveState = getCurrentHomeState().isSearchActive

        homeViewModel.onEvent(HomeEvent.OnSearchClicked)
        val resultActiveState = getCurrentHomeState().isSearchActive

        coVerifyOrder {
            getRecipesUseCase(true,"","") //init
            addSearchSuggestionUseCase(any())
            getRecipesUseCase(false,"","")
        }
        Truth.assertThat(initialActiveState).isTrue()
        Truth.assertThat(resultActiveState).isFalse()
        confirmVerified(
            addSearchSuggestionUseCase,
            getRecipesUseCase
        )
    }

    @Test
    fun `OnClearClicked - query empty and search is active`() {
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(getRecipes()))

        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnActiveChange)
        val initialQueryState = getCurrentHomeState().query
        val initialActiveState = getCurrentHomeState().isSearchActive

        homeViewModel.onEvent(HomeEvent.OnClearClicked)
        val resultQueryState = getCurrentHomeState().query
        val resultActiveState = getCurrentHomeState().isSearchActive

        coVerifyOrder {
            getRecipesUseCase(true,"","") //init
            getRecipesUseCase(false,"","")
        }
        Truth.assertThat(initialQueryState).isEmpty()
        Truth.assertThat(initialActiveState).isTrue()
        Truth.assertThat(resultQueryState).isEmpty()
        Truth.assertThat(resultActiveState).isFalse()
        confirmVerified(getRecipesUseCase)
    }

    @Test
    fun `OnClearClicked - query not empty`() {
        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnQueryChange("Initial query"))
        val initialQueryState = getCurrentHomeState().query
        val initialActiveState = getCurrentHomeState().isSearchActive

        homeViewModel.onEvent(HomeEvent.OnClearClicked)
        val resultQueryState = getCurrentHomeState().query
        val resultActiveState = getCurrentHomeState().isSearchActive

        Truth.assertThat(initialQueryState).isEqualTo("Initial query")
        Truth.assertThat(initialActiveState).isFalse()
        Truth.assertThat(resultQueryState).isEmpty()
        Truth.assertThat(resultActiveState).isFalse()
    }

    @Test
    fun `OnSearchSuggestionClicked - initial query is empty`() {
        homeViewModel = setViewModel()
        val initialQueryState = getCurrentHomeState().query

        homeViewModel.onEvent(HomeEvent.OnSearchSuggestionClicked("Suggestion Text"))
        val resultQueryState = getCurrentHomeState().query

        Truth.assertThat(initialQueryState).isEmpty()
        Truth.assertThat(resultQueryState).isEqualTo("Suggestion Text")
    }

    @Test
    fun `OnSearchSuggestionClicked - initial query is not empty`() {
        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnQueryChange("Initial query"))
        val initialQueryState = getCurrentHomeState().query

        homeViewModel.onEvent(HomeEvent.OnSearchSuggestionClicked("Suggestion Text"))
        val resultQueryState = getCurrentHomeState().query

        Truth.assertThat(initialQueryState).isEqualTo("Initial query")
        Truth.assertThat(resultQueryState).isEqualTo("Suggestion Text")
    }

    @Test
    fun `OnCategoryClicked - category not selected initially`() {
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(getRecipes()))

        homeViewModel = setViewModel()
        val initialCategoryState = getCurrentHomeState().selectedCategory

        homeViewModel.onEvent(HomeEvent.OnCategoryClicked("CategoryId"))
        val resultCategoryState = getCurrentHomeState().selectedCategory

        coVerifyOrder {
            getRecipesUseCase(true,"","") //init
            getRecipesUseCase(false,"","CategoryId")
        }
        Truth.assertThat(initialCategoryState).isEmpty()
        Truth.assertThat(resultCategoryState).isEqualTo("CategoryId")
        confirmVerified(getRecipesUseCase)
    }

    @Test
    fun `OnCategoryClicked - category selected initially`() {
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(getRecipes()))

        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnCategoryClicked("OldCategoryId"))
        val initialCategoryState = getCurrentHomeState().selectedCategory

        homeViewModel.onEvent(HomeEvent.OnCategoryClicked("NewCategoryId"))
        val resultCategoryState = getCurrentHomeState().selectedCategory

        coVerifyOrder {
            getRecipesUseCase(true,"","") //init
            getRecipesUseCase(false,"","OldCategoryId")
            getRecipesUseCase(false,"","NewCategoryId")
        }
        Truth.assertThat(initialCategoryState).isEqualTo("OldCategoryId")
        Truth.assertThat(resultCategoryState).isEqualTo("NewCategoryId")
        confirmVerified(getRecipesUseCase)
    }

    @Test
    fun `onSortRecipes - recipes are sorted in descending order`() {
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(getRecipes()))

        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnSortRecipes(RecipeOrder.DateDescending))
        val result = getCurrentHomeState().recipes

        coVerify { getRecipesUseCase(true,"","") }
        Truth.assertThat(result).isEqualTo(getRecipes().sortedByDescending { it.date })
    }

    @Test
    fun `onSortRecipes - recipes are sorted in ascending order`() {
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(getRecipes()))

        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnSortRecipes(RecipeOrder.DateAscending))
        val result = getCurrentHomeState().recipes

        coVerify { getRecipesUseCase(true,"","") }
        Truth.assertThat(result).isEqualTo(getRecipes().sortedBy { it.date })
    }
}