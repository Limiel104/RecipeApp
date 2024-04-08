package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {

    suspend fun getIngredient(ingredientId: String)
    suspend fun getIngredients(): Flow<Resource<List<Ingredient>>>
}