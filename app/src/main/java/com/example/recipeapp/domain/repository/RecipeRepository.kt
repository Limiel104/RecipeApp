package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getRecipe(recipeId: String)
    suspend fun getRecipes(): Flow<Resource<List<Recipe>>>
}