package com.example.recipeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    @PrimaryKey
    val recipeId: String,
    val name: String,
    val prepTime: String,
    val servings: Int,
    val description: String,
    val isVegetarian: Boolean,
    val isVegan: Boolean,
    val imageUrl: String,
    val createdBy: String,
    val date: Long
)