package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetRecipeUseCase(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipeId: String): Flow<Resource<RecipeWithIngredients>> {
        return recipeRepository.getRecipe(recipeId)
    }
}