package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.RecipeRepository
import com.google.common.truth.Truth.assertThat
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

class GetRecipesUseCaseTest {

    @MockK
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var getRecipesUseCase: GetRecipesUseCase
    private lateinit var recipe: Recipe
    private lateinit var recipe2: Recipe
    private lateinit var recipe3: Recipe

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getRecipesUseCase = GetRecipesUseCase(recipeRepository)

        recipe = Recipe(
            recipeId = "recipeId",
            name = "Recipe Name",
            prepTime = "40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Category", "Category2", "Category3")
        )

        recipe2 = Recipe(
            recipeId = "recipe2Id",
            name = "Recipe 2 Name",
            prepTime = "25 min",
            servings = 1,
            description = "Recipe 2 description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "image2Url",
            createdBy = "userId",
            categories = listOf("Category", "Category3")
        )

        recipe3 = Recipe(
            recipeId = "recipe3Id",
            name = "Recipe 3 Name",
            prepTime = "1 h",
            servings = 6,
            description = "Recipe 3 description",
            isVegetarian = true,
            isVegan = true,
            imageUrl = "image3Url",
            createdBy = "userId",
            categories = listOf("Category4")
        )
    }

    @After
    fun tearDown() {
        confirmVerified(recipeRepository)
        clearAllMocks()
    }

    @Test
    fun `return list of recipes`() {
        val result = Resource.Success(listOf(recipe, recipe2, recipe3))

        coEvery { recipeRepository.getRecipes(any(),any(),any()) } returns flowOf(result)

        val response = runBlocking { getRecipesUseCase(false, "", "").first() }

        coVerify(exactly = 1) { recipeRepository.getRecipes(false, "", "") }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(3)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return list of recipes - only one recipe in the list`() {
        val result = Resource.Success(listOf(recipe))

        coEvery { recipeRepository.getRecipes(any(),any(),any()) } returns flowOf(result)

        val response = runBlocking { getRecipesUseCase(false, "", "").first() }

        coVerify(exactly = 1) { recipeRepository.getRecipes(false, "", "") }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(1)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return list of recipes - no recipes returned`() {
        val result = Resource.Success(emptyList<Recipe>())

        coEvery { recipeRepository.getRecipes(any(),any(),any()) } returns flowOf(result)

        val response = runBlocking { getRecipesUseCase(false, "", "").first() }

        coVerify(exactly = 1) { recipeRepository.getRecipes(false, "", "") }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(0)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery {
            recipeRepository.getRecipes(any(),any(),any())
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { getRecipesUseCase(false, "", "").first() }

        coVerify(exactly = 1) { recipeRepository.getRecipes(false, "", "") }
        assertThat(response).isInstanceOf(Resource.Error::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `getRecipes is loading`() {
        coEvery {
            recipeRepository.getRecipes(any(),any(),any())
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { getRecipesUseCase(false, "", "").first() }

        coVerify(exactly = 1) { recipeRepository.getRecipes(false, "", "") }
        assertThat(response).isInstanceOf(Resource.Loading::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isNull()
    }
}