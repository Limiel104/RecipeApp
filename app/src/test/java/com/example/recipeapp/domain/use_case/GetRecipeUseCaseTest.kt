package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.RecipeRepository
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetRecipeUseCaseTest {

    @MockK
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var getRecipeUseCase: GetRecipeUseCase
    private lateinit var recipeWithIngredients: RecipeWithIngredients

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getRecipeUseCase = GetRecipeUseCase(recipeRepository)

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
        confirmVerified(recipeRepository)
        clearAllMocks()
    }

    @Test
    fun `return recipe`() {
        val result = Resource.Success(recipeWithIngredients)
        coEvery { recipeRepository.getRecipe(any()) } returns flowOf(result)

        val response = runBlocking { getRecipeUseCase("recipeId").first() }

        coVerify(exactly = 1) { recipeRepository.getRecipe("recipeId") }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isInstanceOf(RecipeWithIngredients::class.java)
        Truth.assertThat(response.message).isNull()
    }


    @Test
    fun `return error`() {
        coEvery {
            recipeRepository.getRecipe(any())
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { getRecipeUseCase("recipeId").first() }

        coVerify(exactly = 1) { recipeRepository.getRecipe("recipeId") }
        Truth.assertThat(response).isInstanceOf(Resource.Error::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `getRecipe is loading`() {
        coEvery {
            recipeRepository.getRecipe(any())
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { getRecipeUseCase("recipeId").first() }

        coVerify(exactly = 1) { recipeRepository.getRecipe("recipeId") }
        Truth.assertThat(response).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isNull()
    }
}