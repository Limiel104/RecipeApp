package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.CategoryEntity
import com.example.recipeapp.domain.model.Category

fun CategoryEntity.toCategory(): Category {
    return Category(
        categoryId = categoryId,
        imageUrl = imageUrl
    )
}