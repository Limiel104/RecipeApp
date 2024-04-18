package com.example.recipeapp.presentation.home

import com.example.recipeapp.domain.model.Recipe

data class HomeState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)