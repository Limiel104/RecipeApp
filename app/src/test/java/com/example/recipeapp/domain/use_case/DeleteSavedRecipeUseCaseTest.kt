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

class DeleteSavedRecipeUseCaseTest {

    @MockK
    private lateinit var savedRecipeRepository: SavedRecipeRepository
    private lateinit var deleteSavedRecipeUseCase: DeleteSavedRecipeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        deleteSavedRecipeUseCase = DeleteSavedRecipeUseCase(savedRecipeRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(savedRecipeRepository)
        clearAllMocks()
    }

    @Test
    fun `get true - savedRecipe deleted`() {
        val result = Resource.Success(true)
        coEvery { savedRecipeRepository.deleteSavedRecipe(any()) } returns flowOf(result)

        val response = runBlocking { deleteSavedRecipeUseCase("savedRecipeId").first() }

        coVerify(exactly = 1) { savedRecipeRepository.deleteSavedRecipe("savedRecipeId") }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isTrue()
        Truth.assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery { savedRecipeRepository.deleteSavedRecipe(any()) } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { deleteSavedRecipeUseCase("savedRecipeId").first() }

        coVerify(exactly = 1) { savedRecipeRepository.deleteSavedRecipe("savedRecipeId") }
        Truth.assertThat(response).isInstanceOf(Resource.Error::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `savedRecipe is loading`() {
        coEvery { savedRecipeRepository.deleteSavedRecipe(any()) } returns flowOf(Resource.Loading(true))

        val response = runBlocking { deleteSavedRecipeUseCase("savedRecipeId").first() }

        coVerify(exactly = 1) { savedRecipeRepository.deleteSavedRecipe("savedRecipeId") }
        Truth.assertThat(response).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isNull()
    }
}