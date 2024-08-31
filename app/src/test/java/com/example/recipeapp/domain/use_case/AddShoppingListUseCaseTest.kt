package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.ShoppingListWithIngredients
import com.example.recipeapp.domain.repository.ShoppingListRepository
import com.example.recipeapp.presentation.common.getIngredientsWithBoolean
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

class AddShoppingListUseCaseTest {

    @MockK
    private lateinit var shoppingListRepository: ShoppingListRepository
    private lateinit var addShoppingListUseCase: AddShoppingListUseCase
    private lateinit var shoppingListWithIngredients: ShoppingListWithIngredients

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addShoppingListUseCase = AddShoppingListUseCase(shoppingListRepository)

        shoppingListWithIngredients = ShoppingListWithIngredients(
            shoppingListId = "shoppingListId",
            name = "Shopping List Name",
            createdBy = "userUID",
            ingredients = getIngredientsWithQuantity(),
            checkedIngredients = getIngredientsWithBoolean(),
            date = 1234324354
        )
    }

    @After
    fun tearDown() {
        confirmVerified(shoppingListRepository)
        clearAllMocks()
    }

    @Test
    fun `get true - shoppingList added`() {
        val result = Resource.Success(true)

        coEvery { shoppingListRepository.addShoppingList(any()) } returns flowOf(result)

        val response = runBlocking { addShoppingListUseCase(shoppingListWithIngredients).first() }

        coVerify(exactly = 1) { shoppingListRepository.addShoppingList(shoppingListWithIngredients) }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isTrue()
        Truth.assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery { shoppingListRepository.addShoppingList(any()) } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { addShoppingListUseCase(shoppingListWithIngredients).first() }

        coVerify(exactly = 1) { shoppingListRepository.addShoppingList(shoppingListWithIngredients) }
        Truth.assertThat(response).isInstanceOf(Resource.Error::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `addShoppingList is loading`() {
        coEvery { shoppingListRepository.addShoppingList(any()) } returns flowOf(Resource.Loading(true))

        val response = runBlocking { addShoppingListUseCase(shoppingListWithIngredients).first() }

        coVerify(exactly = 1) { shoppingListRepository.addShoppingList(shoppingListWithIngredients) }
        Truth.assertThat(response).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isNull()
    }
}