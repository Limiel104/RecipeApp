package com.example.recipeapp.domain.model

data class IngredientQuantity(
    val ingredientQuantityId: Long,
    val ingredientId: String,
    val recipeId: String,
    val quantity: String
)