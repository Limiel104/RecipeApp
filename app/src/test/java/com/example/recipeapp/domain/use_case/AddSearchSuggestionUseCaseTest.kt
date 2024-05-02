package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.domain.repository.SearchSuggestionRepository
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

class AddSearchSuggestionUseCaseTest {

    @MockK
    private lateinit var searchSuggestionRepository: SearchSuggestionRepository
    private lateinit var addSearchSuggestionUseCase: AddSearchSuggestionUseCase
    private lateinit var searchSuggestion: SearchSuggestion

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addSearchSuggestionUseCase = AddSearchSuggestionUseCase(searchSuggestionRepository)

        searchSuggestion = SearchSuggestion(
            searchSuggestionId = 1,
            text = "Search Suggestion Text"
        )
    }

    @After
    fun tearDown() {
        confirmVerified(searchSuggestionRepository)
        clearAllMocks()
    }

    @Test
    fun `searchSuggestion was added successfully`() {
        val result = Resource.Success(true)

        coEvery { searchSuggestionRepository.addSearchSuggestion(searchSuggestion) } returns flowOf(result)

        val response = runBlocking { addSearchSuggestionUseCase(searchSuggestion).first() }

        coVerify(exactly = 1) { searchSuggestionRepository.addSearchSuggestion(searchSuggestion) }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isTrue()
        assertThat(response.message).isNull()
    }

    @Test
    fun `searchSuggestion was not added and error was returned`() {
        coEvery {
            searchSuggestionRepository.addSearchSuggestion(searchSuggestion)
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { addSearchSuggestionUseCase(searchSuggestion).first() }

        coVerify(exactly = 1) { searchSuggestionRepository.addSearchSuggestion(searchSuggestion) }
        assertThat(response).isInstanceOf(Resource.Error::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `add searchSuggestion is loading`() {
        coEvery {
            searchSuggestionRepository.addSearchSuggestion(searchSuggestion)
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { addSearchSuggestionUseCase(searchSuggestion).first() }

        coVerify(exactly = 1) { searchSuggestionRepository.addSearchSuggestion(searchSuggestion) }
        assertThat(response).isInstanceOf(Resource.Loading::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isNull()
    }
}