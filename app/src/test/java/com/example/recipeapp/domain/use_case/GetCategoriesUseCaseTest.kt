package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.CategoryRepository
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

class GetCategoriesUseCaseTest {

    @MockK
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var category: Category
    private lateinit var category2: Category
    private lateinit var category3: Category

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCategoriesUseCase = GetCategoriesUseCase(categoryRepository)

        category = Category(
            categoryId = "categoryId",
            imageUrl = "imageUrl"
        )

        category2 = Category(
            categoryId = "category2Id",
            imageUrl = "image2Url"
        )

        category3 = Category(
            categoryId = "category3Id",
            imageUrl = "image3Url"
        )
    }

    @After
    fun tearDown() {
        confirmVerified(categoryRepository)
        clearAllMocks()
    }

    @Test
    fun `return list of categories`() {
        val result = Resource.Success(listOf(category, category2, category3))

        coEvery { categoryRepository.getCategories() } returns flowOf(result)

        val response = runBlocking { getCategoriesUseCase().first() }

        coVerify(exactly = 1) { categoryRepository.getCategories() }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(3)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return list of categories - only one category in the list`() {
        val result = Resource.Success(listOf(category))

        coEvery { categoryRepository.getCategories() } returns flowOf(result)

        val response = runBlocking { getCategoriesUseCase().first() }

        coVerify(exactly = 1) { categoryRepository.getCategories() }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(1)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return list of categories - no categories returned`() {
        val result = Resource.Success(emptyList<Category>())

        coEvery { categoryRepository.getCategories() } returns flowOf(result)

        val response = runBlocking { getCategoriesUseCase().first() }

        coVerify(exactly = 1) { categoryRepository.getCategories() }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(0)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery {
            categoryRepository.getCategories()
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { getCategoriesUseCase().first() }

        coVerify(exactly = 1) { categoryRepository.getCategories() }
        assertThat(response).isInstanceOf(Resource.Error::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `getCategories is loading`() {
        coEvery {
            categoryRepository.getCategories()
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { getCategoriesUseCase().first() }

        coVerify(exactly = 1) { categoryRepository.getCategories() }
        assertThat(response).isInstanceOf(Resource.Loading::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isNull()
    }
}