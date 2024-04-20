package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.SearchQuery
import com.example.recipeapp.domain.repository.SearchQueryRepository
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class GetRecentSearchQueriesUseCase(
    private val searchQueryRepository: SearchQueryRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<SearchQuery>>> {
        return searchQueryRepository.getRecentSearchQueries()
    }
}