package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.ShoppingListWithIngredients
import com.example.recipeapp.domain.repository.ShoppingListRepository
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

class GetShoppingListUseCaseTest {

    @MockK
    private lateinit var shoppingListRepository: ShoppingListRepository
    private lateinit var getShoppingListUseCase: GetShoppingListUseCase
    private lateinit var shoppingListWithIngredients: ShoppingListWithIngredients
    private lateinit var ingredient: Ingredient
    private lateinit var ingredient2: Ingredient

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getShoppingListUseCase = GetShoppingListUseCase(shoppingListRepository)

        ingredient = Ingredient(
            ingredientId = "ingredientId",
            name = "Ingredient Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        ingredient2 = Ingredient(
            ingredientId = "ingredient2Id",
            name = "Ingredient 2 Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        shoppingListWithIngredients = ShoppingListWithIngredients(
            shoppingListId = "shoppingListId",
            name = "Shopping List Name",
            createdBy = "userId",
            ingredients = mapOf(
                ingredient to "3 g",
                ingredient2 to "5 g"
            ),
            checkedIngredients = mapOf(
                ingredient to false,
                ingredient2 to true
            ),
            date = 1234324354
        )
    }

    @After
    fun tearDown() {
        confirmVerified(shoppingListRepository)
        clearAllMocks()
    }

    @Test
    fun `return shoppingListWithIngredients`() {
        val result = Resource.Success(shoppingListWithIngredients)

        coEvery {
            shoppingListRepository.getShoppingList("shoppingListId")
        } returns flowOf(result)

        val response = runBlocking { getShoppingListUseCase("shoppingListId").first() }

        coVerify(exactly = 1) { shoppingListRepository.getShoppingList("shoppingListId") }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isInstanceOf(ShoppingListWithIngredients::class.java)
        Truth.assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery {
            shoppingListRepository.getShoppingList("shoppingListId")
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { getShoppingListUseCase("shoppingListId").first() }

        coVerify(exactly = 1) { shoppingListRepository.getShoppingList("shoppingListId") }
        Truth.assertThat(response).isInstanceOf(Resource.Error::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `getUserShoppingLists is loading`() {
        coEvery {
            shoppingListRepository.getShoppingList("shoppingListId")
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { getShoppingListUseCase("shoppingListId").first() }

        coVerify(exactly = 1) { shoppingListRepository.getShoppingList("shoppingListId") }
        Truth.assertThat(response).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isNull()
    }
}