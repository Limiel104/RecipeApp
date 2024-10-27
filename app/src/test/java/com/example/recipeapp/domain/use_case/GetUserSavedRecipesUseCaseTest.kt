package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import com.example.recipeapp.presentation.common.getRecipes
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

class GetUserSavedRecipesUseCaseTest {

    @MockK
    private lateinit var savedRecipeRepository: SavedRecipeRepository
    private lateinit var getUserSavedRecipesUseCase: GetUserSavedRecipesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getUserSavedRecipesUseCase = GetUserSavedRecipesUseCase(savedRecipeRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(savedRecipeRepository)
        clearAllMocks()
    }

    @Test
    fun `return list of recipes`() {
        val result = Resource.Success(getRecipes())
        coEvery { savedRecipeRepository.getUserSavedRecipes(any(),any(),any()) } returns flowOf(result)

        val response = runBlocking { getUserSavedRecipesUseCase("userUID","",false).first() }

        coVerify(exactly = 1) { savedRecipeRepository.getUserSavedRecipes("userUID","",false) }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isInstanceOf(List::class.java)
        Truth.assertThat(response.data).hasSize(7)
        Truth.assertThat(response.message).isNull()
    }

    @Test
    fun `return list of recipes - only one recipe in the list`() {
        val result = Resource.Success(listOf(getRecipes()[0]))
        coEvery { savedRecipeRepository.getUserSavedRecipes(any(),any(),any()) } returns flowOf(result)

        val response = runBlocking { getUserSavedRecipesUseCase("userUID","",false).first() }

        coVerify(exactly = 1) { savedRecipeRepository.getUserSavedRecipes("userUID","",false) }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isInstanceOf(List::class.java)
        Truth.assertThat(response.data).hasSize(1)
        Truth.assertThat(response.message).isNull()
    }

    @Test
    fun `return list of recipes - no recipes returned`() {
        val result = Resource.Success(emptyList<Recipe>())
        coEvery { savedRecipeRepository.getUserSavedRecipes(any(),any(),any()) } returns flowOf(result)

        val response = runBlocking { getUserSavedRecipesUseCase("userUID","",false).first() }

        coVerify(exactly = 1) { savedRecipeRepository.getUserSavedRecipes("userUID","",false) }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isInstanceOf(List::class.java)
        Truth.assertThat(response.data).hasSize(0)
        Truth.assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery {
            savedRecipeRepository.getUserSavedRecipes(any(),any(),any())
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { getUserSavedRecipesUseCase("userUID","",false).first() }

        coVerify(exactly = 1) { savedRecipeRepository.getUserSavedRecipes("userUID","",false) }
        Truth.assertThat(response).isInstanceOf(Resource.Error::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `getUserSavedRecipes is loading`() {
        coEvery {
            savedRecipeRepository.getUserSavedRecipes(any(),any(),any())
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { getUserSavedRecipesUseCase("userUID","",false).first() }

        coVerify(exactly = 1) { savedRecipeRepository.getUserSavedRecipes("userUID","",false) }
        Truth.assertThat(response).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isNull()
    }
}