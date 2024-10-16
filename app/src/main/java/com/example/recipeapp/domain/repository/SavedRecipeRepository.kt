package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface SavedRecipeRepository {

    suspend fun addSavedRecipe(userId: String, recipeId: String): Flow<Resource<Boolean>>
    suspend fun getUserSavedRecipes(userId: String, query: String, getSavedRecipesFromRemote: Boolean): Flow<Resource<List<Recipe>>>
    suspend fun getSavedRecipeId(userId: String, recipeId: String): Flow<Resource<String>>
    suspend fun deleteSavedRecipe(savedRecipeId: String): Flow<Resource<Boolean>>
}