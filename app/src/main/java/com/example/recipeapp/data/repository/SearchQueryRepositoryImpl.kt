package com.example.recipeapp.data.repository

import com.example.recipeapp.data.local.SearchQueryDao
import com.example.recipeapp.data.mapper.toSearchQuery
import com.example.recipeapp.data.mapper.toSearchQueryEntity
import com.example.recipeapp.domain.model.SearchQuery
import com.example.recipeapp.domain.repository.SearchQueryRepository
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchQueryRepositoryImpl @Inject constructor(
    private val dao: SearchQueryDao
): SearchQueryRepository {
    override suspend fun addSearchQuery(searchQuery: SearchQuery) = flow<Resource<Boolean>> {
        emit(Resource.Loading(true))

        dao.insertSearchQuery(searchQuery.toSearchQueryEntity())
        emit(Resource.Success(true))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getRecentSearchQueries() = flow<Resource<List<SearchQuery>>> {
        emit(Resource.Loading(true))

        val searchQueries = dao.getRecentSearchQueries()
        emit(Resource.Success(searchQueries.map { it.toSearchQuery() }))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}