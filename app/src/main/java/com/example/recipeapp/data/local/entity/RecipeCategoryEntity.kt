package com.example.recipeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int,
    val categoryName: String,
    val recipeId: String
)