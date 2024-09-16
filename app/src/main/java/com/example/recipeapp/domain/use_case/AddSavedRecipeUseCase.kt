package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import kotlinx.coroutines.flow.Flow

class AddSavedRecipeUseCase(
    private val savedRecipeRepository: SavedRecipeRepository
) {
    suspend operator fun invoke(userUID: String, recipeId: String): Flow<Resource<Boolean>> {
        return savedRecipeRepository.addSavedRecipe(userUID, recipeId)
    }
}