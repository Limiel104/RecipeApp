package com.example.recipeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IngredientQuantityEntity(
    @PrimaryKey(autoGenerate = true)
    val ingredientQuantityId: Long,
    val ingredientId: String,
    val recipeId: String,
    val quantity: String
)