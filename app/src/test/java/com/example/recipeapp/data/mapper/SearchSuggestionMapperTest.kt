package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.SearchSuggestionEntity
import com.example.recipeapp.domain.model.SearchSuggestion
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class SearchSuggestionMapperTest {

    private lateinit var searchSuggestion: SearchSuggestion
    private lateinit var searchSuggestionEntity: SearchSuggestionEntity

    @Before
    fun setUp() {
        searchSuggestion = SearchSuggestion(
            searchSuggestionId = 0,
            text = "Search Suggestion Text"
        )

        searchSuggestionEntity = SearchSuggestionEntity(
            searchSuggestionId = 0,
            text = "Search Suggestion Text"
        )
    }

    @Test
    fun `SearchSuggestionEntity can be mapped to SearchSuggestion`() {
        val mappedSearchSuggestion = searchSuggestionEntity.toSearchSuggestion()

        assertThat(mappedSearchSuggestion).isEqualTo(searchSuggestion)
    }

    @Test
    fun `SearchSuggestion can be mapped to SearchSuggestionEntity`() {
        val mappedSearchSuggestionEntity = searchSuggestion.toSearchSuggestionEntity()

        assertThat(mappedSearchSuggestionEntity).isEqualTo(searchSuggestionEntity)
    }
}