package com.example.recipeapp.domain.use_case


import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.IngredientRepository
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

class GetIngredientsUseCaseTest {

    @MockK
    private lateinit var ingredientRepository: IngredientRepository
    private lateinit var getIngredientsUseCase: GetIngredientsUseCase
    private lateinit var ingredient: Ingredient
    private lateinit var ingredient2: Ingredient
    private lateinit var ingredient3: Ingredient

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getIngredientsUseCase = GetIngredientsUseCase(ingredientRepository)

        ingredient = Ingredient(
            ingredientId = "ingredientId",
            name = "Ingredient Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        ingredient2 = Ingredient(
            ingredientId = "ingredient2Id",
            name = "Ingredient 2 Name",
            imageUrl = "image2Url",
            category = "category"
        )

        ingredient3 = Ingredient(
            ingredientId = "ingredient3Id",
            name = "Ingredient 3 Name",
            imageUrl = "image3Url",
            category = "category2"
        )
    }

    @After
    fun tearDown() {
        confirmVerified(ingredientRepository)
        clearAllMocks()
    }

    @Test
    fun `return list of ingredients`() {
        val result = Resource.Success(listOf(ingredient, ingredient2, ingredient3))

        coEvery { ingredientRepository.getIngredients() } returns flowOf(result)

        val response = runBlocking { getIngredientsUseCase().first() }

        coVerify(exactly = 1) { ingredientRepository.getIngredients() }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(3)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return list of ingredients - only one ingredient in the list`() {
        val result = Resource.Success(listOf(ingredient))

        coEvery { ingredientRepository.getIngredients() } returns flowOf(result)

        val response = runBlocking { getIngredientsUseCase().first() }

        coVerify(exactly = 1) { ingredientRepository.getIngredients() }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(1)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return list of ingredients - no ingredients returned`() {
        val result = Resource.Success(emptyList<Ingredient>())

        coEvery { ingredientRepository.getIngredients() } returns flowOf(result)

        val response = runBlocking { getIngredientsUseCase().first() }

        coVerify(exactly = 1) { ingredientRepository.getIngredients() }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(0)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery {
            ingredientRepository.getIngredients()
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { getIngredientsUseCase().first() }

        coVerify(exactly = 1) { ingredientRepository.getIngredients() }
        assertThat(response).isInstanceOf(Resource.Error::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `getIngredients is loading`() {
        coEvery {
            ingredientRepository.getIngredients()
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { getIngredientsUseCase().first() }

        coVerify(exactly = 1) { ingredientRepository.getIngredients() }
        assertThat(response).isInstanceOf(Resource.Loading::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isNull()
    }
}