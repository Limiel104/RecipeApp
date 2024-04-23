package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.repository.RecipeRepository
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class GetRecipesUseCase(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(getRecipesFromRemote: Boolean, query: String, category: String): Flow<Resource<List<Recipe>>> {
        return recipeRepository.getRecipes(getRecipesFromRemote, query, category)
    }
}