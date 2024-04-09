package com.example.recipeapp.data.local.entity

import androidx.room.Entity

@Entity(primaryKeys = ["recipeId","ingredientId"])
data class RecipeIngredientCrossRef(
    val recipeId: String,
    val ingredientId: String
)