package com.example.recipeapp.presentation.account

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetUserRecipesUseCase
import com.example.recipeapp.domain.use_case.LogoutUseCase
import com.example.recipeapp.domain.use_case.SortRecipesUseCase
import com.example.recipeapp.domain.util.RecipeOrder
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
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

class AccountViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var getUserRecipesUseCase: GetUserRecipesUseCase
    private lateinit var sortRecipesUseCase: SortRecipesUseCase
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var recipes: List<Recipe>

    @Before
    fun setUp() {
        sortRecipesUseCase = SortRecipesUseCase()
        getUserRecipesUseCase = mockk()
        getCurrentUserUseCase = mockk()
        logoutUseCase = mockk()
        firebaseUser = mockk()

        every { getCurrentUserUseCase() } returns firebaseUser
        every { firebaseUser.uid } returns "userUID"

        recipes = listOf(
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
                date = 1234567891
            ),
            Recipe(
                recipeId = "recipe5Id",
                name = "Recipe 5 Name",
                prepTime = "15 min",
                servings = 1,
                description = "Recipe 5 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image5Url",
                createdBy = "userId",
                categories = listOf("Category1"),
                date = 1234567894
            ),
            Recipe(
                recipeId = "recipe4Id",
                name = "Recipe 4 Name",
                prepTime = "1 h 15 min",
                servings = 4,
                description = "Recipe 3 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image4Url",
                createdBy = "userId",
                categories = listOf("Category4","Category1","Category2"),
                date = 1234567893
            ),
            Recipe(
                recipeId = "recipeId",
                name = "Recipe Name",
                prepTime = "40 min",
                servings = 4,
                description = "Recipe description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "imageUrl",
                createdBy = "userId",
                categories = listOf("Category", "Category2", "Category3"),
                date = 1234567890
            ),
            Recipe(
                recipeId = "recipe6Id",
                name = "Recipe 6 Name",
                prepTime = "30 min",
                servings = 2,
                description = "Recipe 6 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image6Url",
                createdBy = "userId",
                categories = listOf("Category2"),
                date = 1234567895
            ),
            Recipe(
                recipeId = "recipe3Id",
                name = "Recipe 3 Name",
                prepTime = "1 h",
                servings = 6,
                description = "Recipe 3 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image3Url",
                createdBy = "userId",
                categories = listOf("Category4"),
                date = 1234567892
            )
        )
    }

    @After
    fun tearDown() {
        confirmVerified(getUserRecipesUseCase)
        confirmVerified(getCurrentUserUseCase)
        confirmVerified(logoutUseCase)
        confirmVerified(firebaseUser)
        clearAllMocks()
    }

    private fun setViewModel(): AccountViewModel {
        return AccountViewModel(
            getCurrentUserUseCase,
            getUserRecipesUseCase,
            sortRecipesUseCase,
            logoutUseCase
        )
    }

    private fun getCurrentAccountState(): AccountState {
        return accountViewModel.accountState.value
    }


    private fun verifyMocks() {
        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
        }
    }
    @Test
    fun `getUserRecipes sets recipes successfully`() {
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

        accountViewModel = setViewModel()
        val result = getCurrentAccountState().recipes
        val isLoading = getCurrentAccountState().isLoading

        verifyMocks()
        coVerify(exactly =1) { getUserRecipesUseCase("userUID") }
        assertThat(result).containsExactlyElementsIn(recipes)
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getUserRecipes returns error`() {
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Error("Error message"))

        accountViewModel = setViewModel()
        val result = getCurrentAccountState().recipes
        val isLoading = getCurrentAccountState().isLoading

        verifyMocks()
        coVerify(exactly =1) { getUserRecipesUseCase("userUID") }
        assertThat(result).isEmpty()
        assertThat(isLoading).isFalse()
        confirmVerified(getUserRecipesUseCase)
    }

    @Test
    fun `getUserRecipes is loading`() {
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Loading(true))

        accountViewModel = setViewModel()
        val result = getCurrentAccountState().recipes
        val isLoading = getCurrentAccountState().isLoading

        verifyMocks()
        coVerify(exactly =1) { getUserRecipesUseCase("userUID") }
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
        confirmVerified(getUserRecipesUseCase)
    }

    @Test
    fun `getUserRecipes - recipes are sorted correctly`() {
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

        accountViewModel = setViewModel()
        val result = getCurrentAccountState().recipes
        val isLoading = getCurrentAccountState().isLoading

        verifyMocks()
        coVerify(exactly =1) { getUserRecipesUseCase("userUID") }
        assertThat(result).isEqualTo(recipes.sortedByDescending { it.date })
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `OnSortRecipes - recipes are sorted in descending order`() {
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnSortRecipes(RecipeOrder.DateDescending))
        val result = getCurrentAccountState().recipes

        verifyMocks()
        coVerify(exactly =1) { getUserRecipesUseCase("userUID") }
        assertThat(result).isEqualTo(recipes.sortedByDescending { it.date })
    }

    @Test
    fun `OnSortRecipes - recipes are sorted in ascending order`() {
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnSortRecipes(RecipeOrder.DateAscending))
        val result = getCurrentAccountState().recipes

        verifyMocks()
        coVerify(exactly =1) { getUserRecipesUseCase("userUID") }
        assertThat(result).isEqualTo(recipes.sortedBy { it.date })
    }
}