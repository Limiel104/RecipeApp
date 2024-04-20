package com.example.recipeapp.presentation.home

import com.example.recipeapp.domain.model.Recipe

data class HomeState(
    val recipes: List<Recipe> = emptyList(),
    val query: String = "",
    val isSearchActive: Boolean = false,
    val isLoading: Boolean = false
)