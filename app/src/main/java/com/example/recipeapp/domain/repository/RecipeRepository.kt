package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getRecipe(recipeId: String): Flow<Resource<RecipeWithIngredients>>
    suspend fun getRecipes(): Flow<Resource<List<Recipe>>>
}