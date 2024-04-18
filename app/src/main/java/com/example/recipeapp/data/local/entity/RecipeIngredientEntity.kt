package com.example.recipeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeIngredientId: Long,
    val ingredientId: String,
    val recipeId: String,
    val quantity: String
)