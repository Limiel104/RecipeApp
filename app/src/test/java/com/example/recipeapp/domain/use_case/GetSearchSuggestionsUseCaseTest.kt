package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.domain.repository.SearchSuggestionRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

import org.junit.After
import org.junit.Before
import org.junit.Test

class GetSearchSuggestionsUseCaseTest {

    @MockK
    private lateinit var searchSuggestionRepository: SearchSuggestionRepository
    private lateinit var getSearchSuggestionsUseCase: GetSearchSuggestionsUseCase
    private lateinit var searchSuggestion: SearchSuggestion
    private lateinit var searchSuggestion2: SearchSuggestion
    private lateinit var searchSuggestion3: SearchSuggestion

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getSearchSuggestionsUseCase = GetSearchSuggestionsUseCase(searchSuggestionRepository)

        searchSuggestion = SearchSuggestion(
            searchSuggestionId = 1,
            text = "Search Suggestion Text"
        )

        searchSuggestion2 = SearchSuggestion(
            searchSuggestionId = 2,
            text = "Search Suggestion 2 Text"
        )

        searchSuggestion3 = SearchSuggestion(
            searchSuggestionId = 3,
            text = "Search Suggestion 3 Text"
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `return list of searchSuggestions`() {
        val result = Resource.Success(listOf(searchSuggestion, searchSuggestion2, searchSuggestion3))

        coEvery { searchSuggestionRepository.getSearchSuggestions() } returns flowOf(result)

        val response = runBlocking { getSearchSuggestionsUseCase().first() }

        coVerify(exactly = 1) { getSearchSuggestionsUseCase() }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(3)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return list of searchSuggestions - only one searchSuggestion in the list`() {
        val result = Resource.Success(listOf(searchSuggestion))

        coEvery { searchSuggestionRepository.getSearchSuggestions() } returns flowOf(result)

        val response = runBlocking { getSearchSuggestionsUseCase().first() }

        coVerify(exactly = 1) { getSearchSuggestionsUseCase() }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(1)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return list of searchSuggestions - no searchSuggestions returned`() {
        val result = Resource.Success(emptyList<SearchSuggestion>())

        coEvery { searchSuggestionRepository.getSearchSuggestions() } returns flowOf(result)

        val response = runBlocking { getSearchSuggestionsUseCase().first() }

        coVerify(exactly = 1) { getSearchSuggestionsUseCase() }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(0)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery {
            searchSuggestionRepository.getSearchSuggestions()
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { getSearchSuggestionsUseCase().first() }

        coVerify(exactly = 1) { getSearchSuggestionsUseCase() }
        assertThat(response).isInstanceOf(Resource.Error::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `getSearchSuggestions is loading`() {
        coEvery {
            searchSuggestionRepository.getSearchSuggestions()
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { getSearchSuggestionsUseCase().first() }

        coVerify(exactly = 1) { getSearchSuggestionsUseCase() }
        assertThat(response).isInstanceOf(Resource.Loading::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isNull()
    }
}