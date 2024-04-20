package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.SearchQuery
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchQueryRepository {

    suspend fun addSearchQuery(searchQuery: SearchQuery): Flow<Resource<Boolean>>
    suspend fun getRecentSearchQueries(): Flow<Resource<List<SearchQuery>>>
}