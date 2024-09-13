package com.example.recipeapp.presentation.recipe_details

import androidx.lifecycle.SavedStateHandle
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.GetRecipeUseCase
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecipeDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getRecipeUseCase: GetRecipeUseCase
    private lateinit var recipeDetailsViewModel: RecipeDetailsViewModel
    private lateinit var recipeWithIngredients: RecipeWithIngredients
    private lateinit var emptyRecipeWithIngredients: RecipeWithIngredients
    private lateinit var ingredients: List<Ingredient>

    @Before
    fun setUp() {
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

        ingredients = listOf(
            Ingredient(
                ingredientId = "ingredientId",
                name = "Ingredient Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient2Id",
                name = "Ingredient2 Name",
                imageUrl = "imageUrl",
                category = "category2"
            ),
            Ingredient(
                ingredientId = "ingredient3Id",
                name = "Ingredient3 Name",
                imageUrl = "imageUrl",
                category = "category"
            )
        )

        every { savedStateHandle.get<String>(any()) } returns "recipeId"
    }

    @After
    fun tearDown() {
        confirmVerified(savedStateHandle)
        confirmVerified(getRecipeUseCase)
        clearAllMocks()
    }

    private fun setViewModel(): RecipeDetailsViewModel {
        return RecipeDetailsViewModel(
            savedStateHandle,
            getRecipeUseCase
        )
    }

    private fun getCurrentRecipeDetailsState(): RecipeDetailsState {
        return recipeDetailsViewModel.recipeDetailsState.value
    }

    private fun verifyMocks() {
        coVerifySequence {
            savedStateHandle.get<String>("recipeId")
            getRecipeUseCase("recipeId")
        }
    }

    @Test
    fun `saved state handle is set on init`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Loading(true))
        recipeDetailsViewModel = setViewModel()
        val result = getCurrentRecipeDetailsState().recipeId

        verifyMocks()
        Truth.assertThat(result).isEqualTo("recipeId")
    }

    @Test
    fun `getRecipe runs successfully`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()

        val result = getCurrentRecipeDetailsState().recipe
        val isLoading = getCurrentRecipeDetailsState().isLoading

        verifyMocks()
        Truth.assertThat(result).isEqualTo(recipeWithIngredients)
        Truth.assertThat(isLoading).isFalse()
    }

    @Test
    fun `getRecipe returns error`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Error("Error message"))
        recipeDetailsViewModel = setViewModel()

        val result = getCurrentRecipeDetailsState().recipe
        val isLoading = getCurrentRecipeDetailsState().isLoading

        verifyMocks()
        Truth.assertThat(result).isEqualTo(emptyRecipeWithIngredients)
        Truth.assertThat(isLoading).isFalse()
    }

    @Test
    fun `getRecipe is loading`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Loading(true))
        recipeDetailsViewModel = setViewModel()

        val result = getCurrentRecipeDetailsState().recipe
        val isLoading = getCurrentRecipeDetailsState().isLoading

        verifyMocks()
        Truth.assertThat(result).isEqualTo(emptyRecipeWithIngredients)
        Truth.assertThat(isLoading).isTrue()
    }

    @Test
    fun `getRecipe sets displayed servings correctly`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        val displayedServingsState = getCurrentRecipeDetailsState().displayedServings

        verifyMocks()
        Truth.assertThat(displayedServingsState).isEqualTo(4)
    }

    @Test
    fun `getRecipe sets displayed ingredients correctly`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        val displayedIngredientsState = getCurrentRecipeDetailsState().displayedIngredients

        verifyMocks()
        Truth.assertThat(displayedIngredientsState).isEqualTo(recipeWithIngredients.ingredients)
    }

    @Test
    fun `OnTabChanged - from default tab 0 to tab 1`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        val initialTabState = getCurrentRecipeDetailsState().secondaryTabState

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnTabChanged(1))
        val resultTabState = getCurrentRecipeDetailsState().secondaryTabState

        verifyMocks()
        Truth.assertThat(initialTabState).isEqualTo(0)
        Truth.assertThat(resultTabState).isEqualTo(1)
    }

    @Test
    fun `OnTabChanged - from default tab 0 to tab 0`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        val initialTabState = getCurrentRecipeDetailsState().secondaryTabState

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnTabChanged(0))
        val resultTabState = getCurrentRecipeDetailsState().secondaryTabState

        verifyMocks()
        Truth.assertThat(initialTabState).isEqualTo(0)
        Truth.assertThat(resultTabState).isEqualTo(0)
    }

    @Test
    fun `OnTabChanged - from tab 1 to default tab 0`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnTabChanged(1))
        val initialTabState = getCurrentRecipeDetailsState().secondaryTabState

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnTabChanged(0))
        val resultTabState = getCurrentRecipeDetailsState().secondaryTabState

        verifyMocks()
        Truth.assertThat(initialTabState).isEqualTo(1)
        Truth.assertThat(resultTabState).isEqualTo(0)
    }

    @Test
    fun `OnLessServings - displayed servings is decreased by 1`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        val initialDisplayedServingsState = getCurrentRecipeDetailsState().displayedServings

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnLessServings)
        val resultDisplayedServingsState = getCurrentRecipeDetailsState().displayedServings

        verifyMocks()
        Truth.assertThat(initialDisplayedServingsState).isEqualTo(4)
        Truth.assertThat(resultDisplayedServingsState).isEqualTo(3)
    }

    @Test
    fun `OnLessServings - displayed servings is decreased by 3`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnLessServings)
        val initialDisplayedServingsState = getCurrentRecipeDetailsState().displayedServings

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnLessServings)
        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnLessServings)
        val resultDisplayedServingsState = getCurrentRecipeDetailsState().displayedServings

        verifyMocks()
        Truth.assertThat(initialDisplayedServingsState).isEqualTo(3)
        Truth.assertThat(resultDisplayedServingsState).isEqualTo(1)
    }

    @Test
    fun `OnLessServings - ingredients quantity is recalculated correctly`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        val initialDisplayedIngredientsState = getCurrentRecipeDetailsState().displayedIngredients

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnLessServings)
        val resultDisplayedIngredientsState = getCurrentRecipeDetailsState().displayedIngredients

        verifyMocks()
        Truth.assertThat(initialDisplayedIngredientsState).isEqualTo(recipeWithIngredients.ingredients)
        Truth.assertThat(resultDisplayedIngredientsState).isEqualTo(mapOf(
            Pair(ingredients[0],"150.00 g"),
            Pair(ingredients[1],"3.75 kg"),
            Pair(ingredients[2],"0.75 cup")
        ))
    }

    @Test
    fun `OnLessServings - ingredients quantity is recalculated correctly - 2 times`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnLessServings)
        val initialDisplayedIngredientsState = getCurrentRecipeDetailsState().displayedIngredients

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnLessServings)
        val resultDisplayedIngredientsState = getCurrentRecipeDetailsState().displayedIngredients

        verifyMocks()
        Truth.assertThat(initialDisplayedIngredientsState).isEqualTo(mapOf(
            Pair(ingredients[0],"150.00 g"),
            Pair(ingredients[1],"3.75 kg"),
            Pair(ingredients[2],"0.75 cup")
        ))
        Truth.assertThat(resultDisplayedIngredientsState).isEqualTo(mapOf(
            Pair(ingredients[0],"100.00 g"),
            Pair(ingredients[1],"2.50 kg"),
            Pair(ingredients[2],"0.50 cup")
        ))
    }

    @Test
    fun `OnMoreServings - displayed servings is increased by 1`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        val initialDisplayedServingsState = getCurrentRecipeDetailsState().displayedServings

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnMoreServings)
        val resultDisplayedServingsState = getCurrentRecipeDetailsState().displayedServings

        verifyMocks()
        Truth.assertThat(initialDisplayedServingsState).isEqualTo(4)
        Truth.assertThat(resultDisplayedServingsState).isEqualTo(5)
    }

    @Test
    fun `OnMoreServings - displayed servings is increased by 3`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnMoreServings)
        val initialDisplayedServingsState = getCurrentRecipeDetailsState().displayedServings

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnMoreServings)
        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnMoreServings)
        val resultDisplayedServingsState = getCurrentRecipeDetailsState().displayedServings

        verifyMocks()
        Truth.assertThat(initialDisplayedServingsState).isEqualTo(5)
        Truth.assertThat(resultDisplayedServingsState).isEqualTo(7)
    }

    @Test
    fun `OnMoreServings - ingredients quantity is recalculated correctly`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        val initialDisplayedIngredientsState = getCurrentRecipeDetailsState().displayedIngredients

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnMoreServings)
        val resultDisplayedIngredientsState = getCurrentRecipeDetailsState().displayedIngredients

        verifyMocks()
        Truth.assertThat(initialDisplayedIngredientsState).isEqualTo(recipeWithIngredients.ingredients)
        Truth.assertThat(resultDisplayedIngredientsState).isEqualTo(mapOf(
            Pair(ingredients[0],"250.00 g"),
            Pair(ingredients[1],"6.25 kg"),
            Pair(ingredients[2],"1.25 cup")
        ))
    }

    @Test
    fun `OnMoreServings - ingredients quantity is recalculated correctly - 2 times`() {
        coEvery { getRecipeUseCase(any()) } returns flowOf(Resource.Success(recipeWithIngredients))
        recipeDetailsViewModel = setViewModel()
        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnMoreServings)
        val initialDisplayedIngredientsState = getCurrentRecipeDetailsState().displayedIngredients

        recipeDetailsViewModel.onEvent(RecipeDetailsEvent.OnMoreServings)
        val resultDisplayedIngredientsState = getCurrentRecipeDetailsState().displayedIngredients

        verifyMocks()
        Truth.assertThat(initialDisplayedIngredientsState).isEqualTo(mapOf(
            Pair(ingredients[0],"250.00 g"),
            Pair(ingredients[1],"6.25 kg"),
            Pair(ingredients[2],"1.25 cup")
        ))
        Truth.assertThat(resultDisplayedIngredientsState).isEqualTo(mapOf(
            Pair(ingredients[0],"300.00 g"),
            Pair(ingredients[1],"7.50 kg"),
            Pair(ingredients[2],"1.50 cup")
        ))
    }
}