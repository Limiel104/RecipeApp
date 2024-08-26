package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetUserRecipesUseCase(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(userUID: String): Flow<Resource<List<Recipe>>> {
        return recipeRepository.getUserRecipes(userUID)
    }
}