package com.example.recipeapp.data.repository

import com.example.recipeapp.data.local.CategoryDao
import com.example.recipeapp.data.mapper.toCategory
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.repository.CategoryRepository
import com.example.recipeapp.domain.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
): CategoryRepository {
    override suspend fun getCategories() = flow<Resource<List<Category>>> {
        emit(Resource.Loading(true))

        emit(Resource.Success(dao.getCategories().map { it.toCategory() }))

        emit(Resource.Loading(false))
    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}