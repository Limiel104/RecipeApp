package com.example.recipeapp.presentation.home

import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.SearchSuggestion

data class HomeState(
    val recipes: List<Recipe> = emptyList(),
    val query: String = "",
    val searchSuggestions: List<SearchSuggestion> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: String = "",
    val isSearchActive: Boolean = false,
    val isLoading: Boolean = false
)