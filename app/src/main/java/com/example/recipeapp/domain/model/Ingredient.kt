package com.example.recipeapp.domain.model

data class Ingredient(
    val ingredientId: String,
    val name: String,
    val imageUrl: String,
    val category: String
)