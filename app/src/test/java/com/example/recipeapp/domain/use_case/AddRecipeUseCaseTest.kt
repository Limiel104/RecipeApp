package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.RecipeRepository
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

class AddRecipeUseCaseTest {

    @MockK
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var addRecipeUseCase: AddRecipeUseCase
    private lateinit var recipeWithIngredients: RecipeWithIngredients

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addRecipeUseCase = AddRecipeUseCase(recipeRepository)

        recipeWithIngredients = RecipeWithIngredients(
            recipeId = "recipeId",
            name = "Recipe Name",
            ingredients = mapOf(
                Pair(
                    Ingredient(
                        ingredientId = "ingredientId",
                        name = "Ingredient Name",
                        imageUrl = "imageUrl",
                        category = "category"
                    ),
                    "200.0 g"
                ),
                Pair(
                    Ingredient(
                        ingredientId = "ingredient2Id",
                        name = "Ingredient2 Name",
                        imageUrl = "imageUrl",
                        category = "category"
                    ),
                    "5.0 kg"
                ),
                Pair(
                    Ingredient(
                        ingredientId = "ingredient3Id",
                        name = "Ingredient3 Name",
                        imageUrl = "imageUrl",
                        category = "category"
                    ),
                    "1 cup"
                )
            ),
            prepTime = "40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Category", "Category2", "Category3")
        )
    }

    @After
    fun tearDown() {
        confirmVerified(recipeRepository)
        clearAllMocks()
    }

    @Test
    fun `get true - recipe added`() {
        val result = Resource.Success(true)

        coEvery { recipeRepository.addRecipe(any()) } returns flowOf(result)

        val response = runBlocking { addRecipeUseCase(recipeWithIngredients).first() }

        coVerify(exactly = 1) { recipeRepository.addRecipe(recipeWithIngredients) }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isTrue()
        Truth.assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery { recipeRepository.addRecipe(any()) } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { addRecipeUseCase(recipeWithIngredients).first() }

        coVerify(exactly = 1) { recipeRepository.addRecipe(recipeWithIngredients) }
        Truth.assertThat(response).isInstanceOf(Resource.Error::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `addRecipe is loading`() {
        coEvery { recipeRepository.addRecipe(any()) } returns flowOf(Resource.Loading(true))

        val response = runBlocking { addRecipeUseCase(recipeWithIngredients).first() }

        coVerify(exactly = 1) { recipeRepository.addRecipe(recipeWithIngredients) }
        Truth.assertThat(response).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isNull()
    }
}