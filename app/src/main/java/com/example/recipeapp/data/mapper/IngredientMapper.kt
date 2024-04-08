package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.IngredientEntity
import com.example.recipeapp.domain.model.Ingredient

fun IngredientEntity.toIngredient(): Ingredient {
    return Ingredient(
        id = id,
        name = name,
        imageUrl = imageUrl,
        category = category
    )
}

fun Ingredient.toIngredientEntity(): IngredientEntity {
    return IngredientEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        category = category
    )
}