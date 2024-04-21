package com.example.recipeapp.presentation.home

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.SearchSuggestion

data class HomeState(
    val recipes: List<Recipe> = emptyList(),
    val query: String = "",
    val searchSuggestions: List<SearchSuggestion> = emptyList(),
    val isSearchActive: Boolean = false,
    val isLoading: Boolean = false
)