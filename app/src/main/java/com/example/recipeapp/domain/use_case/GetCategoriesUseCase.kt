package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.repository.CategoryRepository
import com.example.recipeapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Category>>> {
        return categoryRepository.getCategories()
    }
}