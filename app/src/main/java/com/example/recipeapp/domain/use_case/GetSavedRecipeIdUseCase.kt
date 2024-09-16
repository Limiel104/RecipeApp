package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import kotlinx.coroutines.flow.Flow

class GetSavedRecipeIdUseCase(
    private val savedRecipeRepository: SavedRecipeRepository
) {
    suspend operator fun invoke(userUID: String, recipeId: String): Flow<Resource<String>> {
        return savedRecipeRepository.getSavedRecipeId(userUID, recipeId)
    }
}