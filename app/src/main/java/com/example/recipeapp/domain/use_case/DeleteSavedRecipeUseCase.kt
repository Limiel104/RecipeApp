package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import kotlinx.coroutines.flow.Flow

class DeleteSavedRecipeUseCase(
    private val savedRecipeRepository: SavedRecipeRepository
) {
    suspend operator fun invoke(savedRecipeId: String): Flow<Resource<Boolean>> {
        return savedRecipeRepository.deleteSavedRecipe(savedRecipeId)
    }
}