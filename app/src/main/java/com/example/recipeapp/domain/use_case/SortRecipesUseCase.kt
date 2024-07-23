package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.util.RecipeOrder

class SortRecipesUseCase {
    operator fun invoke(recipeOrder: RecipeOrder, recipes: List<Recipe>): List<Recipe> {
        return when(recipeOrder) {
            is RecipeOrder.DateAscending -> recipes.sortedBy { it.date }
            is RecipeOrder.DateDescending -> recipes.sortedByDescending { it.date }
        }
    }
}