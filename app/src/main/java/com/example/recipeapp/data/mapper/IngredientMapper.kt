package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.remote.IngredientDto
import com.example.recipeapp.domain.model.Ingredient

fun IngredientEntity.toIngredient(): Ingredient {
    return Ingredient(
        ingredientId = ingredientId,
        name = name,
        imageUrl = imageUrl,
        category = category
    )
}

fun IngredientDto.toIngredientEntity(): IngredientEntity {
    return IngredientEntity(
        ingredientId = ingredientId,
        name = name,
        imageUrl = imageUrl,
        category = category
    )
}

fun IngredientDto.toIngredient(): Ingredient {
    return Ingredient(
        ingredientId = ingredientId,
        name = name,
        imageUrl = imageUrl,
        category = category
    )
}