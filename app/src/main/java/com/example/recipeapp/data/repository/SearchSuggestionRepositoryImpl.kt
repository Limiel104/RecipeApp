package com.example.recipeapp.data.repository

import com.example.recipeapp.data.local.SearchSuggestionDao
import com.example.recipeapp.data.mapper.toSearchSuggestion
import com.example.recipeapp.data.mapper.toSearchSuggestionEntity
import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.domain.repository.SearchSuggestionRepository
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchSuggestionRepositoryImpl @Inject constructor(
    private val dao: SearchSuggestionDao
): SearchSuggestionRepository {
    override suspend fun addSearchSuggestion(searchSuggestion: SearchSuggestion) = flow<Resource<Boolean>> {
        emit(Resource.Loading(true))

        dao.insertSearchSuggestion(searchSuggestion.toSearchSuggestionEntity())
        emit(Resource.Success(true))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getSearchSuggestions() = flow<Resource<List<SearchSuggestion>>> {
        emit(Resource.Loading(true))

        val searchSuggestions = dao.getSearchSuggestions()
        emit(Resource.Success(searchSuggestions.map { it.toSearchSuggestion() }))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}