package com.example.recipeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedRecipeEntity(
    @PrimaryKey
    val savedRecipeId: String,
    val recipeId: String,
    val userId: String
)