package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.domain.model.Ingredient

fun IngredientEntity.toIngredient(): Ingredient {
    return Ingredient(
        id = ingredientId,
        name = name,
        imageUrl = imageUrl,
        category = category
    )
}

fun Ingredient.toIngredientEntity(): IngredientEntity {
    return IngredientEntity(
        ingredientId = id,
        name = name,
        imageUrl = imageUrl,
        category = category
    )
}