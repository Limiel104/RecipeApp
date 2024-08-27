package com.example.recipeapp.presentation.shopping_list

import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getIngredientsUseCase: GetIngredientsUseCase
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var shoppingListViewModel: ShoppingListViewModel
    private lateinit var ingredients: List<Ingredient>
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp() {
        getIngredientsUseCase = mockk()
        getCurrentUserUseCase = mockk()
        firebaseUser = mockk()

        every { getCurrentUserUseCase() } returns firebaseUser
        every { firebaseUser.uid } returns "userUID"

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
            ),
            Ingredient(
                ingredientId = "ingredient4Id",
                name = "Ingredient 4 Name",
                imageUrl = "image4Url",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient5Id",
                name = "Ingredient 5 Name",
                imageUrl = "image5Url",
                category = "category5"
            )
        )
    }

    @After
    fun tearDown() {
        confirmVerified(getIngredientsUseCase)
        confirmVerified(getCurrentUserUseCase)
        clearAllMocks()
    }

    private fun setViewModel(): ShoppingListViewModel {
        return ShoppingListViewModel(
            getCurrentUserUseCase,
            getIngredientsUseCase
        )
    }

    private fun getCurrentShoppingListState(): ShoppingListState {
        return shoppingListViewModel.shoppingListState.value
    }

    private fun verifyMocks() {
        coVerifySequence {
            getCurrentUserUseCase()
            getIngredientsUseCase()
        }
    }

    @Test
    fun `checkIfUserLoggedIn - user is logged in`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().isUserLoggedIn

        verifyMocks()
        assertThat(result).isTrue()
    }

    @Test
    fun `checkIfUserLoggedIn - user is not logged in`() {
        every { getCurrentUserUseCase() } returns null

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().isUserLoggedIn

        verifySequence { getCurrentUserUseCase() }
        assertThat(result).isFalse()
    }

    @Test
    fun `getIngredients runs successfully`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().ingredientsToSelect
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        assertThat(result).isEqualTo(ingredients)
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getIngredients returns error`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Error("Error message"))

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().ingredientsToSelect
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        assertThat(result).isEmpty()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getIngredients is loading`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Loading(true))

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().ingredientsToSelect
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
    }
}