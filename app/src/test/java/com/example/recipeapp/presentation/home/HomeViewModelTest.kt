package com.example.recipeapp.presentation.home

import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.domain.model.ShoppingList
import com.example.recipeapp.domain.use_case.AddSearchSuggestionUseCase
import com.example.recipeapp.domain.use_case.GetCategoriesUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.domain.use_case.GetRecipesUseCase
import com.example.recipeapp.domain.use_case.GetSearchSuggestionsUseCase
import com.example.recipeapp.domain.use_case.GetUserShoppingListsUseCase
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
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
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recipes: List<Recipe>
    private lateinit var ingredients: List<Ingredient>
    private lateinit var shoppingLists: List<ShoppingList>
    private lateinit var searchSuggestions: List<SearchSuggestion>
    private lateinit var categories: List<Category>

    @Before
    fun setUp() {
        getRecipesUseCase = mockk(relaxed = true)
        getIngredientsUseCase = mockk(relaxed = true)
        getUserShoppingListsUseCase = mockk(relaxed = true)
        addSearchSuggestionUseCase = mockk(relaxed = true)
        getSearchSuggestionsUseCase= mockk(relaxed = true)
        getCategoriesUseCase = mockk(relaxed = true)

        recipes = listOf(
            Recipe(
                recipeId = "recipeId",
                name = "Recipe Name",
                prepTime = "40 min",
                servings = 4,
                description = "Recipe description",
                isVegetarian = true,
                isVegan = false,
                imageUrl = "imageUrl",
                createdBy = "userId",
                categories = listOf("Category", "Category2", "Category3"),
                date = 1234324354
            ),
            Recipe(
                recipeId = "recipe2Id",
                name = "Recipe 2 Name",
                prepTime = "25 min",
                servings = 1,
                description = "Recipe 2 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image2Url",
                createdBy = "userId",
                categories = listOf("Category", "Category3"),
                date = 1234324354
            ),
            Recipe(
                recipeId = "recipe3Id",
                name = "Recipe 3 Name",
                prepTime = "1 h",
                servings = 6,
                description = "Recipe 3 description",
                isVegetarian = true,
                isVegan = true,
                imageUrl = "image3Url",
                createdBy = "userId",
                categories = listOf("Category4"),
                date = 1234324354
            )
        )

        ingredients = listOf(
            Ingredient(
                ingredientId = "ingredientId",
                name = "Ingredient Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient2Id",
                name = "Ingredient 2 Name",
                imageUrl = "image2Url",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient3Id",
                name = "Ingredient 3 Name",
                imageUrl = "image3Url",
                category = "category2"
            )
        )

        shoppingLists = listOf(
            ShoppingList(
                shoppingListId = "shoppingListId",
                name = "Shopping List Name",
                createdBy = "userId"
            ),
            ShoppingList(
                shoppingListId = "shoppingList2Id",
                name = "Shopping List 2 Name",
                createdBy = "userId"
            ),
            ShoppingList(
                shoppingListId = "shoppingList3Id",
                name = "Shopping List 3 Name",
                createdBy = "userId"
            )
        )
        searchSuggestions = listOf(
            SearchSuggestion(
                searchSuggestionId = 1,
                text = "Search Suggestion Text"
            ),
            SearchSuggestion(
                searchSuggestionId = 2,
                text = "Search Suggestion 2 Text"
            ),
            SearchSuggestion(
                searchSuggestionId = 3,
                text = "Search Suggestion 3 Text"
            )
        )

        categories = listOf(
            Category(
                categoryId = "categoryId",
                imageUrl = "imageUrl"
            ),
            Category(
                categoryId = "category2Id",
                imageUrl = "image2Url"
            ),
            Category(
                categoryId = "category3Id",
                imageUrl = "image3Url"
            )
        )
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
            getCategoriesUseCase
        )
    }

    private fun getCurrentHomeState(): HomeState {
        return homeViewModel.homeState.value
    }

    @Test
    fun `getRecipes sets recipes successfully`() {
        coEvery { getRecipesUseCase(any(),any(),any()) } returns flowOf(Resource.Success(recipes))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().recipes
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getRecipesUseCase(true,"","") }
        assertThat(result).isEqualTo(recipes)
        assertThat(isLoading).isFalse()
        confirmVerified(getRecipesUseCase)
    }

    @Test
    fun `getRecipes returns error`() {
        coEvery { getRecipesUseCase(any(),any(),any()) } returns flowOf(Resource.Error("Error message"))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().recipes
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getRecipesUseCase(true,"","") }
        assertThat(result).isEmpty()
        assertThat(isLoading).isFalse()
        confirmVerified(getRecipesUseCase)
    }

    @Test
    fun `getRecipes is loading`() {
        coEvery { getRecipesUseCase(any(),any(),any()) } returns flowOf(Resource.Loading(true))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().recipes
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getRecipesUseCase(true,"","") }
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
        confirmVerified(getRecipesUseCase)
    }

    @Test
    fun `getIngredients runs successfully`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getIngredientsUseCase() }
        assertThat(isLoading).isFalse()
        confirmVerified(getIngredientsUseCase)
    }

    @Test
    fun `getIngredients returns error`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Error("Error message"))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getIngredientsUseCase() }
        assertThat(isLoading).isFalse()
        confirmVerified(getIngredientsUseCase)
    }

    @Test
    fun `getIngredients is loading`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Loading(true))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getIngredientsUseCase() }
        assertThat(isLoading).isTrue()
        confirmVerified(getIngredientsUseCase)
    }

    @Test
    fun `getShoppingLists runs successfully`() {
        coEvery { getUserShoppingListsUseCase("userId",any()) } returns flowOf(Resource.Success(shoppingLists))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getUserShoppingListsUseCase("userId",false) }
        assertThat(isLoading).isFalse()
        confirmVerified(getUserShoppingListsUseCase)
    }

    @Test
    fun `getShoppingLists returns error`() {
        coEvery { getUserShoppingListsUseCase("userId",any()) } returns flowOf(Resource.Error("Error message"))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getUserShoppingListsUseCase("userId",false) }
        assertThat(isLoading).isFalse()
        confirmVerified(getUserShoppingListsUseCase)
    }

    @Test
    fun `getShoppingLists is loading`() {
        coEvery { getUserShoppingListsUseCase("userId",any()) } returns flowOf(Resource.Loading(true))

        homeViewModel = setViewModel()
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getUserShoppingListsUseCase("userId",false) }
        assertThat(isLoading).isTrue()
        confirmVerified(getUserShoppingListsUseCase)
    }

    @Test
    fun `getCategories runs successfully`() {
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Success(categories))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().categories
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getCategoriesUseCase() }
        assertThat(result).isEqualTo(categories)
        assertThat(isLoading).isFalse()
        confirmVerified(getCategoriesUseCase)
    }

    @Test
    fun `getCategories returns error`() {
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Error("Error message"))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().categories
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getCategoriesUseCase() }
        assertThat(result).isEmpty()
        assertThat(isLoading).isFalse()
        confirmVerified(getCategoriesUseCase)
    }

    @Test
    fun `getCategories is loading`() {
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Loading(true))

        homeViewModel = setViewModel()
        val result = getCurrentHomeState().categories
        val isLoading = getCurrentHomeState().isLoading

        coVerify(exactly = 1) { getCategoriesUseCase() }
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
        confirmVerified(getCategoriesUseCase)
    }

    @Test
    fun `onQueryChanged - initial query empty`() {
        homeViewModel = setViewModel()
        val initialQueryState = getCurrentHomeState().query

        homeViewModel.onEvent(HomeEvent.OnQueryChange("New Query"))
        val resultQueryState = getCurrentHomeState().query

        assertThat(initialQueryState).isEmpty()
        assertThat(resultQueryState).isEqualTo("New Query")
    }

    @Test
    fun `onQueryChanged - initial query not empty`() {
        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnQueryChange("Old Query"))
        val initialQueryState = getCurrentHomeState().query

        homeViewModel.onEvent(HomeEvent.OnQueryChange("New Query"))
        val resultQueryState = getCurrentHomeState().query

        assertThat(initialQueryState).isEqualTo("Old Query")
        assertThat(resultQueryState).isEqualTo("New Query")
    }

    @Test
    fun `onActiveChange - initially false`() {
        coEvery { getSearchSuggestionsUseCase() } returns flowOf(Resource.Success(searchSuggestions))

        homeViewModel = setViewModel()
        val initialActiveState = getCurrentHomeState().isSearchActive
        val initialSearchSuggestions = getCurrentHomeState().searchSuggestions

        homeViewModel.onEvent(HomeEvent.OnActiveChange)
        val resultActiveState = getCurrentHomeState().isSearchActive
        val resultSearchSuggestions = getCurrentHomeState().searchSuggestions

        coVerify { getSearchSuggestionsUseCase() }
        assertThat(initialActiveState).isFalse()
        assertThat(initialSearchSuggestions).isEmpty()
        assertThat(resultActiveState).isTrue()
        assertThat(resultSearchSuggestions).isEqualTo(searchSuggestions)
        confirmVerified(getSearchSuggestionsUseCase)
    }

    @Test
    fun `onActiveChange - initially true`() {
        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnActiveChange)
        val initialActiveState = getCurrentHomeState().isSearchActive

        homeViewModel.onEvent(HomeEvent.OnActiveChange)
        val resultActiveState = getCurrentHomeState().isSearchActive

        assertThat(initialActiveState).isTrue()
        assertThat(resultActiveState).isFalse()
    }

    @Test
    fun `onSearchClicked - isActive false`() {
        coEvery { addSearchSuggestionUseCase(any()) } returns flowOf(Resource.Success(true))
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(recipes))

        homeViewModel = setViewModel()
        val initialActiveState = getCurrentHomeState().isSearchActive

        homeViewModel.onEvent(HomeEvent.OnSearchClicked)
        val resultActiveState = getCurrentHomeState().isSearchActive

        coVerifyOrder {
            getRecipesUseCase(true,"","") //init
            addSearchSuggestionUseCase(any())
            getRecipesUseCase(false,"","")
        }
        assertThat(initialActiveState).isFalse()
        assertThat(resultActiveState).isFalse()
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
        } returns flowOf(Resource.Success(recipes))

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
        assertThat(initialActiveState).isTrue()
        assertThat(resultActiveState).isFalse()
        confirmVerified(
            addSearchSuggestionUseCase,
            getRecipesUseCase
        )
    }

    @Test
    fun `OnClearClicked - query empty and search is active`() {
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(recipes))

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
        assertThat(initialQueryState).isEmpty()
        assertThat(initialActiveState).isTrue()
        assertThat(resultQueryState).isEmpty()
        assertThat(resultActiveState).isFalse()
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

        assertThat(initialQueryState).isEqualTo("Initial query")
        assertThat(initialActiveState).isFalse()
        assertThat(resultQueryState).isEmpty()
        assertThat(resultActiveState).isFalse()
    }

    @Test
    fun `OnSearchSuggestionClicked - initial query is empty`() {
        homeViewModel = setViewModel()
        val initialQueryState = getCurrentHomeState().query

        homeViewModel.onEvent(HomeEvent.OnSearchSuggestionClicked("Suggestion Text"))
        val resultQueryState = getCurrentHomeState().query

        assertThat(initialQueryState).isEmpty()
        assertThat(resultQueryState).isEqualTo("Suggestion Text")
    }

    @Test
    fun `OnSearchSuggestionClicked - initial query is not empty`() {
        homeViewModel = setViewModel()
        homeViewModel.onEvent(HomeEvent.OnQueryChange("Initial query"))
        val initialQueryState = getCurrentHomeState().query

        homeViewModel.onEvent(HomeEvent.OnSearchSuggestionClicked("Suggestion Text"))
        val resultQueryState = getCurrentHomeState().query

        assertThat(initialQueryState).isEqualTo("Initial query")
        assertThat(resultQueryState).isEqualTo("Suggestion Text")
    }

    @Test
    fun `OnCategoryClicked - category not selected initially`() {
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(recipes))

        homeViewModel = setViewModel()
        val initialCategoryState = getCurrentHomeState().selectedCategory

        homeViewModel.onEvent(HomeEvent.OnCategoryClicked("CategoryId"))
        val resultCategoryState = getCurrentHomeState().selectedCategory

        coVerifyOrder {
            getRecipesUseCase(true,"","") //init
            getRecipesUseCase(false,"","CategoryId")
        }
        assertThat(initialCategoryState).isEmpty()
        assertThat(resultCategoryState).isEqualTo("CategoryId")
        confirmVerified(getRecipesUseCase)
    }

    @Test
    fun `OnCategoryClicked - category selected initially`() {
        coEvery {
            getRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(recipes))

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
        assertThat(initialCategoryState).isEqualTo("OldCategoryId")
        assertThat(resultCategoryState).isEqualTo("NewCategoryId")
        confirmVerified(getRecipesUseCase)
    }
}