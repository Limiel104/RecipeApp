package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.IngredientQuantityEntity
import com.example.recipeapp.domain.model.IngredientQuantity

fun IngredientQuantityEntity.toIngredientQuantity(recipeId: String): IngredientQuantity {
    return IngredientQuantity(
        ingredientQuantityId = ingredientQuantityId,
        ingredientId = ingredientId,
        recipeId = recipeId,
        quantity = quantity
    )
}