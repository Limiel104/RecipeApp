package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.domain.repository.SearchSuggestionRepository
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class AddSearchSuggestionUseCase(
    private val searchSuggestionRepository: SearchSuggestionRepository
) {
    suspend operator fun invoke(searchSuggestion: SearchSuggestion): Flow<Resource<Boolean>> {
        return searchSuggestionRepository.addSearchSuggestion(searchSuggestion)
    }
}