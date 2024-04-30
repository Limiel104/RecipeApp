package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun getCategories(): Flow<Resource<List<Category>>>
}