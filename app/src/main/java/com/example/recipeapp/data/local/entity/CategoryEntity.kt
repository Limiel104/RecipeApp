package com.example.recipeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey
    val categoryId: String,
    val imageUrl: String
)