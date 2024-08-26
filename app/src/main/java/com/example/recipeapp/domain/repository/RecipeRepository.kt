package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun addRecipe(recipeWithIngredients: RecipeWithIngredients): Flow<Resource<Boolean>>
    suspend fun getRecipe(recipeId: String): Flow<Resource<RecipeWithIngredients>>
    suspend fun getRecipes(getRecipesFromRemote: Boolean, query: String, category: String): Flow<Resource<List<Recipe>>>
    suspend fun getUserRecipes(userUID: String): Flow<Resource<List<Recipe>>>
    suspend fun deleteRecipe(recipeId: String): Flow<Resource<Boolean>>
}