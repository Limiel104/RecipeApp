package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchSuggestionRepository {

    suspend fun addSearchSuggestion(searchSuggestion: SearchSuggestion): Flow<Resource<Boolean>>
    suspend fun getSearchSuggestions(): Flow<Resource<List<SearchSuggestion>>>
}