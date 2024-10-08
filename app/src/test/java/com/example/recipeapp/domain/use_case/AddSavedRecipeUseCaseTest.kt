package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.SavedRecipeRepository
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

class AddSavedRecipeUseCaseTest {

    @MockK
    private lateinit var savedRecipeRepository: SavedRecipeRepository
    private lateinit var addSavedRecipeUseCase: AddSavedRecipeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addSavedRecipeUseCase = AddSavedRecipeUseCase(savedRecipeRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(savedRecipeRepository)
        clearAllMocks()
    }

    @Test
    fun `get true - saved recipe added`() {
        val result = Resource.Success(true)
        coEvery { savedRecipeRepository.addSavedRecipe(any(), any()) } returns flowOf(result)

        val response = runBlocking { addSavedRecipeUseCase("userUID", "recipeId").first() }

        coVerify(exactly = 1) { savedRecipeRepository.addSavedRecipe("userUID", "recipeId") }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isTrue()
        Truth.assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery { savedRecipeRepository.addSavedRecipe(any(), any()) } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { addSavedRecipeUseCase("userUID", "recipeId").first() }

        coVerify(exactly = 1) { savedRecipeRepository.addSavedRecipe("userUID", "recipeId") }
        Truth.assertThat(response).isInstanceOf(Resource.Error::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `addSavedRecipe is loading`() {
        coEvery { savedRecipeRepository.addSavedRecipe(any(), any()) } returns flowOf(Resource.Loading(true))

        val response = runBlocking { addSavedRecipeUseCase("userUID", "recipeId").first() }

        coVerify(exactly = 1) { savedRecipeRepository.addSavedRecipe("userUID", "recipeId") }
        Truth.assertThat(response).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isNull()
    }
}