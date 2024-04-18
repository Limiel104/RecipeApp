package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.repository.IngredientRepository
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class GetIngredientsUseCase(
    private val ingredientRepository: IngredientRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Ingredient>>> {
        return ingredientRepository.getIngredients()
    }
}