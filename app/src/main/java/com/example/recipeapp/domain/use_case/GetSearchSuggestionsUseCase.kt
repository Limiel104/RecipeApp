package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.domain.repository.SearchSuggestionRepository
import com.example.recipeapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

class GetSearchSuggestionsUseCase(
    private val searchSuggestionRepository: SearchSuggestionRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<SearchSuggestion>>> {
        return searchSuggestionRepository.getSearchSuggestions()
    }
}