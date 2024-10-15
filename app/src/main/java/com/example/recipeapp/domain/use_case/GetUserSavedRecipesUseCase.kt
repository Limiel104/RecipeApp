package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import kotlinx.coroutines.flow.Flow

class GetUserSavedRecipesUseCase(
    private val savedRecipeRepository: SavedRecipeRepository
) {
    suspend operator fun invoke(userUID: String, query: String, getSavedRecipesFromRemote: Boolean): Flow<Resource<List<Recipe>>> {
        return savedRecipeRepository.getUserSavedRecipes(userUID, query, getSavedRecipesFromRemote)
    }
}